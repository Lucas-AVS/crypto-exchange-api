package com.lucasavs.cryptoexchange.service;

import com.lucasavs.cryptoexchange.dto.AccountCreateRequest;
import com.lucasavs.cryptoexchange.dto.AccountDto;
import com.lucasavs.cryptoexchange.dto.AccountUpdateRequest;
import com.lucasavs.cryptoexchange.entity.Account;
import com.lucasavs.cryptoexchange.entity.Asset;
import com.lucasavs.cryptoexchange.entity.User;
import com.lucasavs.cryptoexchange.exception.ResourceAlreadyExistException;
import com.lucasavs.cryptoexchange.exception.ResourceNotFoundException;
import com.lucasavs.cryptoexchange.mapper.AccountMapper;
import com.lucasavs.cryptoexchange.repository.AccountRepository;
import com.lucasavs.cryptoexchange.repository.AssetRepository;
import com.lucasavs.cryptoexchange.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository,
                              AssetRepository assetRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.assetRepository = assetRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    @Transactional
    public AccountDto create(UUID authenticatedUserId, AccountCreateRequest request) {
        User user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found."));

        Asset asset = assetRepository.findBySymbol(request.getAssetSymbol())
                .orElseThrow(() -> new ResourceNotFoundException("Asset with symbol " + request.getAssetSymbol() + " not found."));

        Account newAccount = new Account(user, asset);

        try {
            Account savedAccount = accountRepository.save(newAccount);
            return accountMapper.toDto(savedAccount);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExistException("Account for user " + authenticatedUserId +
                    " and asset " + request.getAssetSymbol() + " already exists.");
        }
    }

    @Override
    @Transactional
    public AccountDto update(UUID authenticatedUserId, String assetSymbol, AccountUpdateRequest request) {
        Account accountFromDb = accountRepository.findByUserIdAndAssetSymbol(authenticatedUserId, assetSymbol)
                .orElseThrow(() -> new ResourceNotFoundException("Account for asset " + assetSymbol + " not found for this user."));

        if (request.getBalance() != null) {
            accountFromDb.setBalance(request.getBalance());
        }

        try {
            Account savedAccount = accountRepository.save(accountFromDb);
            return accountMapper.toDto(savedAccount);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("This account was updated by another operation."); // todo: create a better exception
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> findByUserId(UUID userId) {
        return accountRepository.findByUserId(userId).stream()
                .map(accountMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDto findByUserIdAndAssetSymbol(UUID userId, String assetSymbol) {
        Optional<Account> optionalAccount = accountRepository.findByUserIdAndAssetSymbol(userId, assetSymbol);
        Account account = optionalAccount.orElseThrow(
                () -> new ResourceNotFoundException("Account for user " + userId + " and asset " + assetSymbol + " not found."));
        return accountMapper.toDto(account);
    }
}