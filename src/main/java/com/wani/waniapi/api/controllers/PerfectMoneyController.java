package com.wani.waniapi.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kong.unirest.GetRequest;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

@RestController
@RequestMapping("/api/test")
public class PerfectMoneyController {
	
	private String accountId = "8888";
	private String passPhrase = "fake_pass_phrase";
	
	public PerfectMoneyController() {
		// TODO Auto-generated constructor stub
		Unirest.config().defaultBaseUrl("https://perfectmoney.is");
		System.out.print("perfect money controller initializing...");
	}
	
	 @GetMapping("/pm/balance")
	 public ResponseEntity getBalance() {
		 HttpResponse json =  Unirest.get("/acct/balance.asp?PassPhrase=" + this.passPhrase + "&AccountID="+this.accountId).asString();
//		 GetRequest json =  Unirest.get("/acct/balance.asp?PassPhrase=" + this.passPhrase + "&AccountID="+this.accountId);
		 System.out.println(json.getStatus());
		 System.out.println(json.getBody());
		 System.out.println(json.toString());
//		 System.out.println(json);
		 return ResponseEntity.ok("request to get perfect money balance");
		 
	 }

}
