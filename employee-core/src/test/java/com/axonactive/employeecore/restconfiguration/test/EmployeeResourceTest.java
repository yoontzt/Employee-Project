/*
 * package com.axonactive.employeecore.restconfiguration.test;
 * 
 * import static org.junit.Assert.assertEquals;
 * 
 * import java.util.Arrays; import java.util.List; import java.util.Objects;
 * import java.util.Optional;
 * 
 * import javax.ws.rs.core.Response;
 * 
 * import org.apache.logging.log4j.Logger; import org.junit.Test; import
 * org.junit.runner.RunWith; import org.mockito.InjectMocks; import
 * org.mockito.Mock; import org.mockito.Mockito; import
 * org.powermock.modules.junit4.PowerMockRunner;
 * 
 * import com.axonactive.employeecore.converter.EmployeeConverter; import
 * com.axonactive.employeecore.dto.DepartmentDTO; import
 * com.axonactive.employeecore.dto.EmployeeDTO; import
 * com.axonactive.employeecore.exception.ValidationException; import
 * com.axonactive.employeecore.restconfiguration.EmployeeResource; import
 * com.axonactive.employeecore.service.EmployeeService;
 * 
 * @RunWith(PowerMockRunner.class) public class EmployeeResourceTest {
 * 
 * @InjectMocks EmployeeResource employeeResource;
 * 
 * @Mock EmployeeConverter employeeConverter;
 * 
 * @Mock EmployeeService employeeService;
 * 
 * @Mock Logger logger;
 * 
 * @Test public void testGetAllList_ShouldReturnEmployeeList_WhenListExist() {
 * List<EmployeeDTO> expected = Arrays.asList(createEmployeeDTO(),
 * createEmployeeDTO()); List<EmployeeDTO> employeeDTO =
 * Arrays.asList(createEmployeeDTO(), createEmployeeDTO());
 * 
 * Mockito.when(employeeService.getAllEmployeeList()).thenReturn(employeeDTO);
 * assertEquals(expected, employeeResource.getAllList()); }
 * 
 * @Test public void
 * testGetEmployeeById_ShouldReturnOptionalEntity_WhenValidEmployeeIdIsGiven() {
 * Optional<EmployeeDTO> optionalEmp = createOptionalEmployeeDTO();
 * Mockito.when(employeeService.findEmployeeById(1)).thenReturn(optionalEmp);
 * Response actual = employeeResource.getEmployeeById(1); assertEquals(200,
 * actual.getStatus()); assertEquals(createEmployeeDTO(), actual.getEntity()); }
 * 
 * @Test(expected = ValidationException.class) public void
 * testGetEmployeeById_ShouldReturnErrorStatusResponse_WhenNoExistingEmployeeIdIsGiven
 * () { Mockito.when(employeeService.findEmployeeById(100)).thenThrow(new
 * ValidationException("No ID"));
 * Mockito.verify(employeeService.findEmployeeById(100));
 * employeeResource.getEmployeeById(100); }
 * 
 * @Test public void
 * testAddEmployee_ShouldReturnOKStatusResponse_WhenEmployeeDTOIsGiven() {
 * Response actual = employeeResource.addEmployee(createEmployeeDTO());
 * Mockito.verify(employeeService).addEmployee(createEmployeeDTO());
 * assertEquals(200, actual.getStatus()); }
 * 
 * @Test(expected = ValidationException.class) public void
 * testAddEmployee_ShouldthrowException_WhenDuplicateEmailIsAdded() {
 * Mockito.when(employeeService.addEmployee(null)).thenThrow(new
 * ValidationException("email is duplicated."));
 * employeeResource.addEmployee(null); }
 * 
 * @Test public void
 * testUpdateEmployee_ShouldReturnOKStatusResponse_WhenEmployeeDTOIsGiven() {
 * Response actual = employeeResource.updateEmployee(createEmployeeDTO());
 * Mockito.verify(employeeService).updateEmployee(createEmployeeDTO());
 * assertEquals(200, actual.getStatus()); }
 * 
 * @Test public void
 * testDeleteEmployeeById_ShouldReturnOKStatus_WhenValidEmployeeIdIsGiven() {
 * Response actual = employeeResource.deleteEmployeebyId(1);
 * Mockito.verify(employeeService).deleteEmployeeById(1); assertEquals(200,
 * actual.getStatus()); }
 * 
 * private DepartmentDTO createDepartmentDTO() { return new DepartmentDTO(1,
 * "ICT"); }
 * 
 * private EmployeeDTO createEmployeeDTO() { DepartmentDTO department =
 * createDepartmentDTO(); return new EmployeeDTO(1, "Yoon", "20",
 * "yoon@gmail.com", department); }
 * 
 * private Optional<EmployeeDTO> createOptionalEmployeeDTO() { EmployeeDTO
 * employee = createEmployeeDTO(); if (Objects.isNull(employee)) { return
 * Optional.empty(); } return Optional.of(employee); } }
 */