package com.lucasavs.cryptoexchange.mapper;

import com.lucasavs.cryptoexchange.dto.AccountDto;
import com.lucasavs.cryptoexchange.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public AccountDto toDto(Account account) {
        AccountDto dto = new AccountDto();
        dto.setId(account.getId());
        dto.setUserId((account.getUser().getId()));
        dto.setAssetSymbol(account.getAssetSymbol());
        dto.setBalance(account.getBalance());
        return dto;
    }
}
