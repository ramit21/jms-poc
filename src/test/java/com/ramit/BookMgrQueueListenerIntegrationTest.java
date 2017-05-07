package com.ramit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import javax.jms.Destination;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import com.ramit.domain.BookDTO;
import com.ramit.jms.BookMgrQueueListener;
import com.ramit.jms.OperationEnum;
import com.ramit.service.BookService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JmsPocApplication.class)
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
// Pick different application.properties for test cases
public class BookMgrQueueListenerIntegrationTest {

	@Autowired(required = false)
	private JmsTemplate jmsTemplate;

	@Autowired
	private BookMgrQueueListener bookMgrQueueListener;

	@Autowired(required = false)
	@Qualifier("bookMgrQueueDestination")
	private Destination bookMgrQueueDestination;

	@Mock
	private BookService mockBookService;

	@Captor
	private ArgumentCaptor<BookDTO> bookArgumentCaptor;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(bookMgrQueueListener, "bookService", mockBookService);
	}

	@Test
	public void testSendCreateBookMessage() {
		BookDTO book = new BookDTO("isbn", "title", "author");
		jmsTemplate.convertAndSend(bookMgrQueueDestination, book, Message -> {
			return OperationEnum.CREATE.applyToMessage(Message);
		});
		// verify
		verify(mockBookService).createNew(bookArgumentCaptor.capture());
		assertEquals(book.getIsbn(), bookArgumentCaptor.getValue().getIsbn());
		assertEquals(book.getTitle(), bookArgumentCaptor.getValue().getTitle());
		assertEquals(book.getAuthor(), bookArgumentCaptor.getValue().getAuthor());
	}

	@Test
	public void testSendUpdateBookMessage() {
		BookDTO book = new BookDTO("isbn", "title", "author");
		jmsTemplate.convertAndSend(bookMgrQueueDestination, book, Message -> {
			return OperationEnum.UPDATE.applyToMessage(Message);
		});
		// verify
		verify(mockBookService).update(eq(book.getIsbn()), bookArgumentCaptor.capture());
		assertEquals(book.getIsbn(), bookArgumentCaptor.getValue().getIsbn());
		assertEquals(book.getTitle(), bookArgumentCaptor.getValue().getTitle());
		assertEquals(book.getAuthor(), bookArgumentCaptor.getValue().getAuthor());
	}

	@Test
	public void testSendDeleteBookMessage() {
		BookDTO book = new BookDTO("isbn", "title", "author");
		jmsTemplate.convertAndSend(bookMgrQueueDestination, book, Message -> {
			return OperationEnum.DELETE.applyToMessage(Message);
		});
		// verify
		verify(mockBookService).delete(book.getIsbn());
	}
}
