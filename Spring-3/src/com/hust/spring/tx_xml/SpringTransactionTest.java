package com.hust.spring.tx_xml;

import com.hust.spring.tx_xml.service.BookShopDao;
import com.hust.spring.tx_xml.service.BookShopService;
import com.hust.spring.tx_xml.service.Cashier;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;


public class SpringTransactionTest {

    private ApplicationContext ctx = null;
    private BookShopDao bookShopDao = null;
    private BookShopService bookShopService = null;
    private Cashier cashier;

    {
        ctx = new ClassPathXmlApplicationContext("applicationContext-tx-xml.xml");
        bookShopDao = ctx.getBean(BookShopDao.class);
        bookShopService = ctx.getBean(BookShopService.class);
        cashier = ctx.getBean(Cashier.class);
    }

    @Test
    public void testTransactionalPropagation(){
        cashier.checkout("mzyan", Arrays.asList("1001", "1002"));
    }

    @Test
    public void testBookShopService(){
        bookShopService.purchase("mzyan", "1001");
    }

    @Test
    public void testBookShopDaoUpdateUserAccount(){
        bookShopDao.updateUserAccount("mzyan", 200);
    }

    @Test
    public void testBookShopDaoUpdateBookStock(){
        bookShopDao.updateBookStock("1001");
    }

    @Test
    public void testBookShopDaoFindPriceByIsbn(){
        System.out.println(bookShopDao.findBookPriceByisbn("1001"));
    }
}
