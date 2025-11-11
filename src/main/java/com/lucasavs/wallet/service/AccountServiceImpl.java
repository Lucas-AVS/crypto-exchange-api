package com.lucasavs.wallet.service;

import com.lucasavs.wallet.client.AssetServiceClient;
import com.lucasavs.wallet.dto.AccountCreateRequest;
import com.lucasavs.wallet.dto.AccountDto;
import com.lucasavs.wallet.dto.AccountUpdateRequest;
import com.lucasavs.wallet.dto.AssetDto;
import com.lucasavs.wallet.entity.Account;
import com.lucasavs.wallet.entity.User;
import com.lucasavs.wallet.exception.InsufficientFundsException;
import com.lucasavs.wallet.exception.ResourceAlreadyExistException;
import com.lucasavs.wallet.exception.ResourceNotFoundException;
import com.lucasavs.wallet.mapper.AccountMapper;
import com.lucasavs.wallet.repository.AccountRepository;
import com.lucasavs.wallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AssetServiceClient assetServiceClient;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository,
                              AssetServiceClient assetServiceClient, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.assetServiceClient = assetServiceClient;
        this.accountMapper = accountMapper;
    }

    @Override
    public AccountDto create(UUID authenticatedUserId, AccountCreateRequest request) {
        User user = userRepository.findById(authenticatedUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found."));

        AssetDto asset = assetServiceClient.findAssetBySymbol(request.getAssetSymbol())
                .orElseThrow(() -> new ResourceNotFoundException("Asset with symbol " + request.getAssetSymbol() + " not found."));

        Account newAccount = new Account(user, asset.getSymbol());
        if (request.getBalance() != null && request.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            newAccount.setBalance(request.getBalance());
        }
        try {
            Account savedAccount = accountRepository.save(newAccount);
            return accountMapper.toDto(savedAccount);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExistException("Account for user " + authenticatedUserId +
                    " and asset " + request.getAssetSymbol() + " already exists.");
        }
    }

    @Override
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
    public List<AccountDto> findByUserId(UUID userId) {
        return accountRepository.findByUserId(userId).stream()
                .map(accountMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto findByUserIdAndAssetSymbol(UUID userId, String assetSymbol) {
        Optional<Account> optionalAccount = accountRepository.findByUserIdAndAssetSymbol(userId, assetSymbol);
        Account account = optionalAccount.orElseThrow(
                () -> new ResourceNotFoundException("Account for user " + userId + " and asset " + assetSymbol + " not found."));
        return accountMapper.toDto(account);
    }

    @Override
    public AccountDto updateBalance(UUID userId, String assetSymbol, BigDecimal amountDelta) {

        Account account = accountRepository.findByUserIdAndAssetSymbol(userId, assetSymbol)
                .orElse(null);

        if (account == null) {
            if (amountDelta.compareTo(BigDecimal.ZERO) < 0) {
                throw new ResourceNotFoundException(
                        "Cannot update balance: Account for user " + userId + " and asset " + assetSymbol + " not found."
                );
            }

            AccountCreateRequest request = new AccountCreateRequest();
            request.setBalance(amountDelta);
            request.setAssetSymbol(assetSymbol);
            return this.create(userId, request);
        }

        BigDecimal newBalance = account.getBalance().add(amountDelta);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException(
                    "Insufficient funds for user " + userId + ". Cannot apply delta of " + amountDelta + " to asset " + assetSymbol
            );
        }

        account.setBalance(newBalance);
        Account updatedAccount = accountRepository.save(account);
        return accountMapper.toDto(updatedAccount);
    }
}