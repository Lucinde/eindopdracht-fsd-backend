package com.lucinde.plannerpro.services;

import com.lucinde.plannerpro.dtos.CustomerDto;
import com.lucinde.plannerpro.dtos.TaskDto;
import com.lucinde.plannerpro.exceptions.RecordNotFoundException;
import com.lucinde.plannerpro.models.Customer;
import com.lucinde.plannerpro.models.Task;
import com.lucinde.plannerpro.repositories.CustomerRepository;
import com.lucinde.plannerpro.repositories.TaskRepository;
import com.lucinde.plannerpro.utils.PageResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;
    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    TaskService taskService;

    @Captor
    ArgumentCaptor<Task> taskArgumentCaptor;

    List<Task> taskList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Customer customer = new Customer();

        Task task1 = new Task();
        task1.setId(1L);
        task1.setDescription("Task 1 description");
        task1.setWorkPerformed("Task 1 work performed");
        task1.setJobDone(false);
        task1.setCustomer(customer);
        taskList.add(task1);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setDescription("Task 2 description");
        task2.setWorkPerformed("Task 2 work performed");
        task2.setJobDone(true);
        task2.setCustomer(customer);
        taskList.add(task2);

        Task task3 = new Task();
        task3.setId(3L);
        task3.setDescription("Task 3 description");
        task3.setWorkPerformed("Task 3 work performed");
        task3.setJobDone(false);
        task3.setCustomer(customer);
        taskList.add(task3);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllTasks() {
        when(taskRepository.findAll()).thenReturn(taskList);

        List<TaskDto> taskDtos = taskService.getAllTasks();

        assertEquals(taskList.size(), taskDtos.size());
    }

    @Test
    void getTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskList.get(0)));

        TaskDto taskDto = taskService.getTask(1L);

        assertEquals(taskList.get(0).getDescription(), taskDto.description);
        assertEquals(taskList.get(0).getWorkPerformed(), taskDto.workPerformed);
        assertEquals(taskList.get(0).getJobDone(), taskDto.jobDone);
        assertEquals(taskList.get(0).getCustomer(), taskDto.customer);
    }

    @Test
    void getTaskThrowsException() {
        assertThrows(RecordNotFoundException.class, () -> taskService.getTask(9L));
    }

    @Test
    void getTasksWithPagination() {
        int pageNo = 0;
        int pageSize = 3;
        Page<Task> taskPage = new PageImpl<>(taskList);
        when(taskRepository.findAll(any(PageRequest.class))).thenReturn(taskPage);

        PageResponse<TaskDto> pageResponse = taskService.getTasksWithPagination(pageNo, pageSize);

        assertNotNull(pageResponse);
        assertEquals(taskList.size(), pageResponse.count);
        assertEquals(1, pageResponse.totalPages);
        assertFalse(pageResponse.hasPrevious);
        assertFalse(pageResponse.hasNext);
        for (int i = 0; i < taskList.size(); i++) {
            assertEquals(taskList.get(i).getDescription(), pageResponse.items.get(i).description);
            assertEquals(taskList.get(i).getWorkPerformed(), pageResponse.items.get(i).workPerformed);
            assertEquals(taskList.get(i).getJobDone(), pageResponse.items.get(i).jobDone);
            assertEquals(taskList.get(i).getCustomer(), pageResponse.items.get(i).customer);
        }
    }

    @Test
    void addTask() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void deleteTask() {
    }

    @Test
    void transferTaskToDto() {
    }

    @Test
    void transferDtoToTask() {
    }

    @Test
    void testTransferDtoToTask() {
    }
}