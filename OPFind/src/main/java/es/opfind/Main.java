package es.opfind;

import java.util.Calendar;
import java.util.Date;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.opfind.service.crawler.OpCrawler;

/**
 * 
 * @author Joan Navarrete Sanchez
 *
 */
public class Main {

	/**
	 * Main Lets harvest the BOE!!!!!! 
	 * @param args
	 */
	public static void main(String[] args) {
	
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	    OpCrawler opCrawler = (OpCrawler)ctx.getBean("OpCrawler");
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(2010, Calendar.APRIL, 17);
	    while(true){
		    opCrawler.storeCivilJobs(calendar.getTime());
		    calendar.add(Calendar.DAY_OF_YEAR, -1);
	    }
	}

}
