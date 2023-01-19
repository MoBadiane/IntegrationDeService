package com.esmt.m223isi.microservices.currencyconversionservice.controller;


import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.esmt.m223isi.microservices.currencyconversionservice.bean.CurrencyConversion;
import com.esmt.m223isi.microservices.currencyconversionservice.proxy.CurrencyExchangeProxy;


@RestController
public class CurrencyConversionController {

	@Autowired 
	private Environment environment;
	
	@Autowired
	private CurrencyExchangeProxy proxy ;
	//@GetMapping("/currency-conversion/from/{from}/to/{to}/amount/{amount}")
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/amount/{amount}")
	//CurrencyConversion getCurrencyConversion(
	CurrencyConversion getCurrencyConversionFeign(
			@PathVariable String from ,@PathVariable String to,@PathVariable BigDecimal amount) {
	/*	
		HashMap<String,String> uniVariables =new HashMap<>();
		uniVariables.put("from", from);
		uniVariables.put("to", to);
		
		ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().
				getForEntity("http://localhost:9000/currency-exchange/from/{from}/to/{to}",
						CurrencyConversion.class,uniVariables);
		
		CurrencyConversion currencyConversion=responseEntity.getBody();   */
		CurrencyConversion currencyConversion=proxy.getCurrencyExchange(from, to);
		String port =environment.getProperty("local.server.port");
		return new CurrencyConversion(currencyConversion.getId(),
				from,to,amount,currencyConversion.getRateExchange(),
				amount.multiply(currencyConversion.getRateExchange()),"Feign REST Client",port);
		
		
		//return new CurrencyConversion(1001L,from,to,amount,BigDecimal.TEN,BigDecimal.TEN.multiply(amount));
	}
	
	@GetMapping("/currency-conversion/from/{from}/to/{to}/amount/{amount}")
	CurrencyConversion getCurrencyConversion(
			@PathVariable String from ,@PathVariable String to,@PathVariable BigDecimal amount) {
		HashMap<String,String> uniVariables =new HashMap<>();
		uniVariables.put("from", from);
		uniVariables.put("to", to);
		
		ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().
				getForEntity("http://localhost:9000/currency-exchange/from/{from}/to/{to}",
						CurrencyConversion.class,uniVariables);
		
		CurrencyConversion currencyConversion=responseEntity.getBody();
		//currencyConversion.setClient("REST Template");
		String port =environment.getProperty("local.server.port");
		return new CurrencyConversion(currencyConversion.getId(),
				from,to,amount,currencyConversion.getRateExchange(),
				amount.multiply(currencyConversion.getRateExchange()),"REST Template",port);
	}
}
