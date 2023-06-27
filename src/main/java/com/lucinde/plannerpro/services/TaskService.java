package com.lucinde.plannerpro.services;

import com.lucinde.plannerpro.dtos.TaskDto;
import com.lucinde.plannerpro.exceptions.RecordNotFoundException;
import com.lucinde.plannerpro.models.Customer;
import com.lucinde.plannerpro.models.Task;
import com.lucinde.plannerpro.repositories.CustomerRepository;
import com.lucinde.plannerpro.repositories.TaskRepository;
import com.lucinde.plannerpro.utils.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final CustomerRepository customerRepository;

    public TaskService(TaskRepository taskRepository, CustomerRepository customerRepository) {
        this.taskRepository = taskRepository;
        this.customerRepository = customerRepository;
    }

    public List<TaskDto> getAllTasks() {
        Iterable<Task> tasks = taskRepository.findAll();
        List<TaskDto> taskDtos = new ArrayList<>();

        for (Task t: tasks) {
            taskDtos.add(transferTaskToDto(t));
        }

        return taskDtos;
    }

    public TaskDto getTask(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);

        if(taskOptional.isEmpty()) {
            throw new RecordNotFoundException("Geen taak gevonden met id: " + id);
        }

        Task task = taskOptional.get();

        return transferTaskToDto(task);
    }

    public PageResponse<TaskDto> getTasksWithPagination(int pageNo, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("id").descending());
        Page<Task> pagingTask = taskRepository.findAll(pageRequest);

        PageResponse<TaskDto> response = new PageResponse<>();

        response.count = pagingTask.getTotalElements();
        response.totalPages = pagingTask.getTotalPages();
        response.hasNext = pagingTask.hasNext();
        response.hasPrevious = pagingTask.hasPrevious();
        response.items = new ArrayList<>();

        for (Task t: pagingTask) {
            response.items.add(transferTaskToDto(t));
        }

        return response;
    }

    public TaskDto addTask(TaskDto taskDto) {
        Task task = transferDtoToTask(taskDto);
        taskRepository.save(task);

        return transferTaskToDto(task);
        //todo: OPT: DTO maken waarbij alleen een customerId (evt zonder ScheduleTaskList omdat die bij het toevoegen nog leeg zal zijn) worden meegegeven of een optie maken waarbij hij de gegevens van de customer ophaalt nav het ID. Nu geeft hij overal null aan terwijl de waardes er wel zijn en dat kan verwarrend zijn. Omdat we in de front-end de JSON niet te zien krijgen heeft dit nog even lagere prioriteit, wellicht is het wel nodig om de juiste gegevens terug te krijgen dus dan kan de prio weer omhoog.
    }

    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Task updateTask = transferDtoToTask(taskDto, id);
        updateTask.setId(id);

        Customer customer = customerRepository.findById(taskDto.customer.getId())
                .orElseThrow(() -> new RecordNotFoundException("Klant niet gevonden"));

        if(taskDto.customer.getFirstName() != null)
            customer.setFirstName(taskDto.customer.getFirstName());
        if(taskDto.customer.getLastName() != null)
            customer.setLastName(taskDto.customer.getLastName());
        if(taskDto.customer.getAddress() != null)
            customer.setAddress(taskDto.customer.getAddress());
        if(taskDto.customer.getZip() != null)
            customer.setZip(taskDto.customer.getZip());
        if(taskDto.customer.getCity() != null)
            customer.setCity(taskDto.customer.getCity());
        if(taskDto.customer.getPhoneNumber() != null)
            customer.setPhoneNumber(taskDto.customer.getPhoneNumber());
        if(taskDto.customer.getEmail() != null)
            customer.setEmail(taskDto.customer.getEmail());
        if(taskDto.customer.getTaskList() != null)
            customer.setTaskList(taskDto.customer.getTaskList());

        updateTask.setCustomer(customer);

        taskRepository.save(updateTask);

        return transferTaskToDto(updateTask);
    }

    //todo: assignTaskToCustomer toevoegen! Customers kunnen nu nog niet aan een taak toegewezen worden

    public void deleteTask(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if(optionalTask.isEmpty()) {
            throw new RecordNotFoundException("Geen taak gevonden met id: " + id);
        }
        taskRepository.deleteById(id);
    }

    public TaskDto transferTaskToDto(Task task) {
        TaskDto taskDto = new TaskDto();

        taskDto.id = task.getId();
        taskDto.description = task.getDescription();
        taskDto.workPerformed = task.getWorkPerformed();
        taskDto.jobDone = task.getJobDone();
        taskDto.customer = task.getCustomer();
        taskDto.fileList = task.getFileList();
        taskDto.scheduleTaskList = task.getScheduleTaskList();

        return taskDto;
    }

    public Task transferDtoToTask(TaskDto taskDto) {
        return transferDtoToTask(taskDto, 0L);
    }

    public Task transferDtoToTask(TaskDto taskDto, Long id) {
        Task task;

        if(id != 0L) {
            Optional<Task> taskOptional = taskRepository.findById(id);
            if(taskOptional.isEmpty()) {
                throw new RecordNotFoundException("Geen taak gevonden met id: " + id);
            }
            task = taskOptional.get();
        } else {
            task = new Task();
        }

        // Geen setId nodig, deze genereert de database of staat in de URL
        if(taskDto.description != null)
            task.setDescription(taskDto.description);
        if(taskDto.workPerformed != null)
            task.setWorkPerformed(taskDto.workPerformed);
        if(taskDto.jobDone != null)
            task.setJobDone(taskDto.jobDone);
        if(taskDto.customer != null)
            task.setCustomer(taskDto.customer);
        if(taskDto.scheduleTaskList != null)
            task.setFileList(taskDto.fileList);
        if(taskDto.scheduleTaskList != null)
            task.setScheduleTaskList(taskDto.scheduleTaskList);

        return task;
    }

}

