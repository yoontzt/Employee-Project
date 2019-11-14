package com.axonactive.employeecore.arquillian.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.assertj.core.api.Assertions;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.axonactive.employeecore.additionalcontact.ContactDTO;
import com.axonactive.employeecore.additionalcontact.ContactService;
import com.axonactive.employeecore.additionalcontact.ContactType;
import com.axonactive.employeecore.exception.EntityNotFoundException;
import com.axonactive.employeecore.exception.ValidationException;

@RunWith(Arquillian.class)
@CleanupUsingScript("scripts/cleanup.sql") 
@Cleanup(phase = TestExecutionPhase.BEFORE)
@UsingDataSet("contacts.yml")
public class ContactServiceArquillianTest {
	@EJB 
	ContactService contactService;
	
	@Deployment
    public static WebArchive createDeployment() {
		// Import Maven runtime dependencies
		
        File[] files = Maven.resolver()
                            .loadPomFromFile("pom.xml")
                            .importCompileAndRuntimeDependencies()
                            .resolve()
                            .withTransitivity()
                            .asFile(); 
		WebArchive archive = ShrinkWrap.create(WebArchive.class) ;
        archive	.addPackages(true, Filters.exclude("com.axonactive.employeecore.restconfiguration*"), "com.axonactive.employeecore")
        	.addAsLibraries(files)
        	.addPackages(true, "org.apache.log4j")
        	.addAsResource("test-persistence.xml", "META-INF/persistence.xml") 
        	.addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml");
        	//.addAsResource("META-INF/beans.xml");
        	
        //System.out.println(archive.toString(true));
        return archive;
    }
	@Test
	public void findAllContactByEmployeeId_hasSomeContact() {
		
		List<ContactDTO> expectedList = new ArrayList<ContactDTO>();
		expectedList.add(new ContactDTO(1,ContactType.EMAIL.getValue(),"other.email-with-hyphen@example.com"));
		expectedList.add(new ContactDTO(2,ContactType.LINK.getValue(),"https://twitter.com/codyogden"));
		List<ContactDTO> actualContactList = contactService.findAllContactByEmployeeId(1);
		Assertions.assertThat(actualContactList)
        .usingElementComparatorOnFields("type", "value")
        .containsExactlyInAnyOrderElementsOf(expectedList);
	}
	@Test
	public void findAllContactByEmployeeId_hasNoContact() {
		List<ContactDTO> actualContactList = contactService.findAllContactByEmployeeId(2);
		assertTrue(actualContactList.isEmpty());
	}
	@Test (expected = EntityNotFoundException.class)
	public void findAllContactByEmployeeId_employeeIDNotExist() {
		List<ContactDTO> actualContactList = contactService.findAllContactByEmployeeId(3);
		assertTrue(actualContactList.isEmpty());
	}
	@Test
	public void findAContactByEmployeeIDCertificateID_valid() {
		ContactDTO contact = new ContactDTO(1,ContactType.EMAIL.getValue(),"other.email-with-hyphen@example.com");
		ContactDTO actualContact = contactService.findAContactOfEmployee(1, 1);
		
		Assertions.assertThat(actualContact).isEqualToIgnoringGivenFields(contact, "contactId");
	}
	@Test (expected = EntityNotFoundException.class)
	public void findAContactByEmployeeIDContactID_invalidContactId() {
		
		contactService.findAContactOfEmployee(1, 3);
		
	}
	@Test (expected = EntityNotFoundException.class)
	public void findAContactByEmployeeIDContactID_invalidEmployeeId() {
		
		contactService.findAContactOfEmployee(3, 3);
		
	}
	
	@Test
	@ShouldMatchDataSet(value = "datasets/expected-contact-after-add.yml", excludeColumns = "contactId")
	public void addContact_validData() {
		ContactDTO contact = new ContactDTO();
		contact.setType(ContactType.PHONE.getValue());
		contact.setValue("+49-89-636-4801");
		contactService.addContact(2,contact);
	} 
	@Test (expected = ValidationException.class)
	@ShouldMatchDataSet(value = "datasets/contacts.yml", excludeColumns = "contactId")
	public void addContact_emptyValue() {
		ContactDTO contact = new ContactDTO();
		contact.setType(ContactType.PHONE.getValue());
		
		contactService.addContact(2,contact);
	}
	@Test (expected = ValidationException.class)
	@ShouldMatchDataSet(value = "datasets/contacts.yml", excludeColumns = "contactId")
	public void addContact_emptyType() {
		ContactDTO contact = new ContactDTO();
		contact.setValue("https://codepen.io/codyogden");
		
		contactService.addContact(2,contact);
	}
	@Test (expected = ValidationException.class)
	@ShouldMatchDataSet(value = "datasets/contacts.yml", excludeColumns = "contactId")
	public void addContact_notExistedContactType() {
		ContactDTO contact = new ContactDTO();
		contact.setContactId(1);
		contact.setType("Zalo");
		contact.setValue("https://codepen.io/codyogden");
		
		contactService.addContact(2,contact);
	}
	@Test (expected = ValidationException.class)
	@ShouldMatchDataSet(value = "datasets/contacts.yml", excludeColumns = "contactId")
	public void addContact_wrongValueFormat() {
		ContactDTO contact = new ContactDTO();
		contact.setContactId(1);
		contact.setType(ContactType.EMAIL.getValue());
		contact.setValue("https://codepen.io/codyogden");
		
		contactService.addContact(2,contact);
	}
	@Test (expected = ValidationException.class)
	@ShouldMatchDataSet(value = "datasets/contacts.yml", excludeColumns = "contactId")
	public void addContact_existingContactId() {
		ContactDTO contact = new ContactDTO();
		contact.setContactId(1);
		contact.setType(ContactType.LINK.getValue());
		contact.setValue("https://codepen.io/codyogden");
		
		contactService.addContact(2,contact);
	}
	@Test (expected = EntityNotFoundException.class)
	@ShouldMatchDataSet(value = "datasets/contacts.yml", excludeColumns = "contactId")
	public void addContact_notExistedEmployeeId() {
		ContactDTO contact = new ContactDTO();
		contact.setContactId(1);
		contact.setType(ContactType.LINK.getValue());
		contact.setValue("https://codepen.io/codyogden");
		
		contactService.addContact(3,contact);
	}
	@Test (expected = ValidationException.class)
	@ShouldMatchDataSet(value = "datasets/contacts.yml", excludeColumns = "contactId")
	public void addContact_longValue() {
		ContactDTO contact = new ContactDTO();
		contact.setContactId(1);
		contact.setType(ContactType.LINK.getValue());
		contact.setValue("http://www.google.co.uk/#sclient=psy-ab&hl=en&source=hp&q=ASUSTeK+Computer+INC.+Model+M4A78LT-M+manual&pbx=1&oq=ASUSTeK+Computer+INC.+Model+M4A78LT-M+manual&aq=f&aqi=&aql=&gs_sm=3&gs_upl=52765l57528l0l57848l8l8l0l0l0l0l2413l3989l8-1.1l2l0&bav=on.2,or.r_gc.r_pw.,cf.osb&fp=3d6c1d1d0a5ea45f&biw=1262&bih=879\r\n");
		
		contactService.addContact(2,contact);
	}
	@Test
	@ShouldMatchDataSet(value = "datasets/expected-contact-after-update.yml", excludeColumns = "contactId")
	public void updateContact_validData_changeValueOnly() {
		ContactDTO contact = new ContactDTO(1, ContactType.EMAIL.getValue(), "fully-qualified-domain@example.com");
		contactService.updateContact(contact, 1);
	}
}
