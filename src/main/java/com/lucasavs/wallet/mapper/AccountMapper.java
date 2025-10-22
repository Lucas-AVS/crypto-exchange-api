package com.lucasavs.wallet.mapper;

import com.lucasavs.wallet.dto.AccountDto;
import com.lucasavs.wallet.entity.Account;
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
