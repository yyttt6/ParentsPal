package com.example.main;

import com.example.main.dao.login.Parent;
import com.example.main.dao.login.ParentRepository;
import com.example.main.dao.login.BabyRepository;
import com.example.main.dto.login.LoginDTO;
import com.example.main.dto.login.ParentDTO;
import com.example.main.response.LoginMessage;
import org.jasypt.encryption.StringEncryptor;
import com.example.main.service.encry.EncryptionService;
import com.example.main.service.login.ParentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ParentServiceImplTest {

	@Mock
	private ParentRepository parentRepository;

	@Mock
	private BabyRepository babyRepository;

	@InjectMocks
	private ParentServiceImpl parentService;
	@Mock
	private StringEncryptor stringEncryptor;

    private ParentDTO parentDTO;
	private LoginDTO loginDTO;
	private Parent parent;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
        EncryptionService encryptionService = new EncryptionService(stringEncryptor);
		// Prepare mock data
		parentService = new ParentServiceImpl(encryptionService);
		parentService.setParentRepository(parentRepository);
		parentService.setBabyRepository(babyRepository);
		parentDTO = new ParentDTO( "John Doe", "1234567890", "password");
		loginDTO = new LoginDTO("1234567890", "password");
		parent = new Parent(1L, "John Doe", "1234567890", "password");
	}


	@Test
	public void testAddNewAppUser_Success() {
		when(parentRepository.existsByPhoneNumber(parentDTO.getPhoneNumber())).thenReturn(false);
		when(parentRepository.save(any(Parent.class))).thenReturn(parent);

		LoginMessage loginMessage = parentService.addNewAppUser(parentDTO);

		assertTrue(loginMessage.getStatus());
		assertEquals("Registration successful", loginMessage.getMessage());
		assertEquals(parent.getId(), loginMessage.getParentId());
		assertEquals(parent.getName(), loginMessage.getParentName());
	}

	@Test
	public void testLoginAppUser_Success() {
		when(stringEncryptor.encrypt(anyString())).thenReturn("encryptedValue");
		when(stringEncryptor.decrypt(anyString())).thenReturn("password");
		when(parentRepository.findByPhoneNumber(loginDTO.getPhoneNumber())).thenReturn(parent);
		when(parentRepository.findOneByPhoneNumberAndPassword(loginDTO.getPhoneNumber(),loginDTO.getPassword())).thenReturn(Optional.ofNullable(parent));

		LoginMessage loginMessage = parentService.loginAppUser(loginDTO);


		assertTrue(loginMessage.getStatus());
		assertEquals("Login Success. Parent ID: " + parent.getId(), loginMessage.getMessage());
	}

	@Test
	public void testLoginAppUser_Failure_InvalidPassword() {
		when(stringEncryptor.encrypt(anyString())).thenReturn("encryptedValue");
		when(stringEncryptor.decrypt(anyString())).thenReturn("password");
		when(parentRepository.findByPhoneNumber(loginDTO.getPhoneNumber())).thenReturn(parent);

		loginDTO.setPassword("wrongpassword");

		LoginMessage loginMessage = parentService.loginAppUser(loginDTO);

		assertFalse(loginMessage.getStatus());
		assertEquals("password Not Match", loginMessage.getMessage());
	}

	@Test
	public void testChangePassword_Success() {
		when(stringEncryptor.encrypt(anyString())).thenReturn("encryptedValue");
		when(stringEncryptor.decrypt(anyString())).thenReturn("password");
		when(parentRepository.findById(parent.getId())).thenReturn(java.util.Optional.of(parent));

		LoginMessage loginMessage = parentService.changePassword(parent.getId(), "password", "newpassword");

		assertTrue(loginMessage.getStatus());
		assertEquals("Password changed successfully", loginMessage.getMessage());
	}

	@Test
	public void testChangePassword_Failure_WrongOldPassword() {
		when(stringEncryptor.encrypt(anyString())).thenReturn("encryptedValue");
		when(stringEncryptor.decrypt(anyString())).thenReturn("password");
		when(parentRepository.findById(parent.getId())).thenReturn(java.util.Optional.of(parent));

		LoginMessage loginMessage = parentService.changePassword(parent.getId(), "wrongpassword", "newpassword");

		assertFalse(loginMessage.getStatus());
		assertEquals("The original password is wrong.", loginMessage.getMessage());
	}
}


