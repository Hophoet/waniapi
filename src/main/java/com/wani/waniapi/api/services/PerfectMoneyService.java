package com.wani.waniapi.api.services;

import java.time.*;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.wani.waniapi.api.models.Account;
import com.wani.waniapi.api.models.AccountResponse;
import com.wani.waniapi.api.repositories.AccountRepository;
import com.wani.waniapi.auth.models.User;
import com.wani.waniapi.auth.repository.UserRepository;

@Service
public class PerfectMoneyService {

	@Value("${PERFECT_MONEY_ACCOUNT_ID}")
	private String PERFECT_MONEY_ACCOUNT_ID;
	
	@Value("${PERFECT_MONEY_PASS_PHRASE}")
	private String PERFECT_MONEY_PASS_PHRASE;

	@Value("${PERFECT_MONEY_WITHDRAWAL_MEMO}")
	private String PERFECT_MONEY_WITHDRAWAL_MEMO;

	@Value("${PERFECT_MONEY_DEPOSIT_ACCOUNT}")
	private String PERFECT_MONEY_DEPOSIT_ACCOUNT;
	
	@Value("${PERFECT_MONEY_WITHDRAWAL_ACCOUNT}")
	private String PERFECT_MONEY_WITHDRAWAL_ACCOUNT;

	public String getPERFECT_MONEY_ACCOUNT_ID() {
		return PERFECT_MONEY_ACCOUNT_ID;
	}

	public void setPERFECT_MONEY_ACCOUNT_ID(String pERFECT_MONEY_ACCOUNT_ID) {
		PERFECT_MONEY_ACCOUNT_ID = pERFECT_MONEY_ACCOUNT_ID;
	}

	public String getPERFECT_MONEY_PASS_PHRASE() {
		return PERFECT_MONEY_PASS_PHRASE;
	}

	public void setPERFECT_MONEY_PASS_PHRASE(String pERFECT_MONEY_PASS_PHRASE) {
		PERFECT_MONEY_PASS_PHRASE = pERFECT_MONEY_PASS_PHRASE;
	}

	public String getPERFECT_MONEY_WITHDRAWAL_MEMO() {
		return PERFECT_MONEY_WITHDRAWAL_MEMO;
	}

	public void setPERFECT_MONEY_WITHDRAWAL_MEMO(String pERFECT_MONEY_WITHDRAWAL_MEMO) {
		PERFECT_MONEY_WITHDRAWAL_MEMO = pERFECT_MONEY_WITHDRAWAL_MEMO;
	}

	public String getPERFECT_MONEY_DEPOSIT_ACCOUNT() {
		return PERFECT_MONEY_DEPOSIT_ACCOUNT;
	}

	public void setPERFECT_MONEY_DEPOSIT_ACCOUNT(String pERFECT_MONEY_DEPOSIT_ACCOUNT) {
		PERFECT_MONEY_DEPOSIT_ACCOUNT = pERFECT_MONEY_DEPOSIT_ACCOUNT;
	}

	public String getPERFECT_MONEY_WITHDRAWAL_ACCOUNT() {
		return PERFECT_MONEY_WITHDRAWAL_ACCOUNT;
	}

	public void setPERFECT_MONEY_WITHDRAWAL_ACCOUNT(String pERFECT_MONEY_WITHDRAWAL_ACCOUNT) {
		PERFECT_MONEY_WITHDRAWAL_ACCOUNT = pERFECT_MONEY_WITHDRAWAL_ACCOUNT;
	}
}