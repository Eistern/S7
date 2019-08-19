package com.contacts.demo.controllers;

import com.contacts.demo.data.JpaNameRepository;
import com.contacts.demo.data.types.Person;
import com.contacts.demo.elasticsearch.PersonSearchEntity;
import com.contacts.demo.elasticsearch.SearchRepository;
import com.contacts.demo.kafka.MessageTypes;
import com.contacts.demo.kafka.PersonUpdateMessage;
import com.contacts.demo.security.data.types.UserEntry;
import com.hazelcast.core.HazelcastInstance;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RegexpQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/person", produces = "application/json")
@CrossOrigin("*")
public class PersonController {
    private final static String TOPIC = "person-update";
    private final JpaNameRepository nameRepositoryJPA;
    private final Logger log = Logger.getLogger(PersonController.class.getName());
    private final KafkaTemplate<String, PersonUpdateMessage> kafkaUpdate;
    private final HazelcastInstance hazelcastInstance;
    private final SearchRepository searchRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public PersonController(JpaNameRepository nameRepositoryJPA, KafkaTemplate<String, PersonUpdateMessage> kafkaUpdate, HazelcastInstance hazelcastInstance, SearchRepository searchRepository, ElasticsearchTemplate elasticsearchTemplate) {
        this.nameRepositoryJPA = nameRepositoryJPA;
        this.kafkaUpdate = kafkaUpdate;
        this.hazelcastInstance = hazelcastInstance;
        this.searchRepository = searchRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
        elasticsearchTemplate.createIndex(PersonSearchEntity.class);
    }

    @GetMapping
    public Iterable<Person> showPersons(@AuthenticationPrincipal UserEntry userEntry) {
        Iterable<Person> result = nameRepositoryJPA.findAllByUid(userEntry.getUid());
        log.info("ShowPersons executed");
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> showPersonById(@PathVariable("id") @NumberFormat Integer id, @AuthenticationPrincipal UserEntry userEntry) {
        Optional<Person> result;
        if ((result = nameRepositoryJPA.findByPersonIdAndUid(id, userEntry.getUid())).isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        kafkaUpdate.send(TOPIC, new PersonUpdateMessage(MessageTypes.ACCESS, userEntry.getUid(), result.get()));
        incrementProduced();
        log.info("ShowPersonById executed with id=" + id);
        return new ResponseEntity<>(result.get(), HttpStatus.OK);
    }

    @Transactional
    @PostMapping(consumes = "application/json")
    public Person addPerson(@RequestBody @Valid Person newPerson, @AuthenticationPrincipal UserEntry userEntry) {
        newPerson.setUid(userEntry.getUid());
        Person result = nameRepositoryJPA.save(newPerson);

        PersonSearchEntity searchEntity = new PersonSearchEntity(result.getPersonId(), result.getName());
        PersonSearchEntity test = searchRepository.save(searchEntity);

        kafkaUpdate.send(TOPIC, new PersonUpdateMessage(MessageTypes.CREATED, userEntry.getUid(), newPerson));
        incrementProduced();
        log.info("AddPerson executed. " + newPerson + " added");
        return newPerson;
    }

    @Transactional
    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Person> editPerson(@PathVariable("id") @NumberFormat @NotEmpty Integer id, @RequestBody @NotEmpty Person newPerson, @AuthenticationPrincipal UserEntry userEntry) {
        Optional<Person> foundPerson = nameRepositoryJPA.findByPersonIdAndUid(id, userEntry.getUid());
        if (foundPerson.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Person editablePerson = foundPerson.get();
        String correctName = editablePerson.getName();
        editablePerson.setName(correctName);

        if (newPerson.getName() != null)
            editablePerson.setName(newPerson.getName());
        Person resultedPerson = nameRepositoryJPA.save(editablePerson);

        Optional<PersonSearchEntity> searchEntity = searchRepository.findById(id);
        if (searchEntity.isEmpty())
            searchRepository.save(new PersonSearchEntity(resultedPerson.getPersonId(), resultedPerson.getName()));
        else {
            PersonSearchEntity foundEntity = searchEntity.get();
            foundEntity.setName(resultedPerson.getName());
            foundEntity.setPersonId(resultedPerson.getPersonId());
            searchRepository.save(foundEntity);
        }

        kafkaUpdate.send(TOPIC, new PersonUpdateMessage(MessageTypes.UPDATE, userEntry.getUid(), resultedPerson));
        incrementProduced();
        log.info("EditPerson executed. Id=" + id + " updated. Now :" + newPerson);
        return new ResponseEntity<>(resultedPerson, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Person>> searchPersons(String name, @AuthenticationPrincipal UserEntry userEntry) {
        SearchQuery query = new NativeSearchQueryBuilder().withQuery(
                QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("name", name).operator(Operator.AND)
                        .autoGenerateSynonymsPhraseQuery(true)
                        .operator(Operator.AND)
                        .analyzer("english")
                        .operator(Operator.AND)
                        .fuzziness(Fuzziness.ONE)))
                .build();
        List<PersonSearchEntity> searchEntities = elasticsearchTemplate.queryForList(query, PersonSearchEntity.class);
        if (searchEntities == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<Person> searchResults = new ArrayList<>();
        searchEntities.forEach(personSearchEntity -> {
            ResponseEntity<Person> getResult = showPersonById(personSearchEntity.getPersonId(), userEntry);
            if (getResult.getStatusCode() == HttpStatus.OK)
                searchResults.add(getResult.getBody());
        });

        if (searchResults.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(searchResults, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable("id") @NumberFormat Integer id, @AuthenticationPrincipal UserEntry userEntry) {
        if (!nameRepositoryJPA.existsByPersonIdAndUid(id, userEntry.getUid()))
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        nameRepositoryJPA.deleteById(id);

        kafkaUpdate.send(TOPIC, new PersonUpdateMessage(MessageTypes.DELETE, userEntry.getUid(), new Person(id)));
        incrementProduced();
        searchRepository.deleteById(id);
        log.info("DeleteById executed. Id=" + id + " deleted");
        return new ResponseEntity(HttpStatus.OK);
    }

    private void incrementProduced() {
        Map<String, Integer> kafkaCount = hazelcastInstance.getMap("kafka-count");
        Integer sentCount = kafkaCount.getOrDefault("produced-person", 0);
        kafkaCount.put("produced-person", sentCount + 1);
    }
}
