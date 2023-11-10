package se.lexicon.g46todoapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.lexicon.g46todoapi.domain.entity.Person;
import se.lexicon.g46todoapi.domain.entity.Task;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TaskRepositoryTest {

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private PersonRepository personRepository;

  @BeforeEach
  public void setUp() {
    // Create a person
    Person person = new Person("John");
    person = personRepository.save(person);

    // Create and save some Tasks with various states and deadlines
    taskRepository.save(new Task("Task 1", "Title 1", LocalDate.parse("2023-11-10"), person));
    taskRepository.save(new Task("Task 1", "Title 1", LocalDate.parse("2023-11-10"), person));
    taskRepository.save(new Task("Task 1", "Title 1", LocalDate.parse("2023-11-15"), true, person));
    taskRepository.save(new Task("Task 1", "Title 1", LocalDate.parse("2023-10-11")));
  }

  @Test
  public void testFindByTitleContains() {
    // Act
    List<Task> foundTasks = taskRepository.findByTitleContains("Task");

    // Assert
    assertEquals(4, foundTasks.size()); // All tasks have "Task" in their title
  }

  @Test
  public void testFindByPerson_Id() {
    // Arrange
    Person person = personRepository.save(new Person("Alice"));

    // Act
    List<Task> foundItems = taskRepository.findByPerson_Id(person.getId());

    // Assert
    assertEquals(0, foundItems.size()); // Person "Alice" has no tasks in the test setup
  }

  @Test
  public void testFindByDone() {
    // Act
    List<Task> doneItems = taskRepository.findByDone(true);

    // Assert
    assertEquals(1, doneItems.size()); // Only "Task 2" is done
  }

  @Test
  public void testFindByDeadlineBetween() {
    // Act
    LocalDate from = LocalDate.parse("2023-10-10");
    LocalDate to = LocalDate.parse("2023-11-11");
    List<Task> items = taskRepository.findByDeadlineBetween(from, to);

    // Assert
    assertEquals(3, items.size());
  }

  @Test
  public void testFindByPersonIsNull() {
    // Act
    List<Task> items = taskRepository.findByPersonIsNull();

    // Assert
    assertEquals(1, items.size()); // "Task 4" has no associated person
  }

  @Test
  public void testFindAllUnfinishedAndOverdue() {
    // Act
    List<Task> unfinishedAndOverdue = taskRepository.selectUnFinishedAndOverdueTasks();

    // Assert
    assertEquals(1, unfinishedAndOverdue.size()); // Only "Task 3" is unfinished and overdue
  }
}
