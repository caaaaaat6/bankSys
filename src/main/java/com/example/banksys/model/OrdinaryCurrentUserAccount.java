package com.example.banksys.model;

public class OrdinaryCurrentUserAccount extends CurrentUserAccount {


    @Override
    public long openAccount(long userId, String userPid, String userName, String userType, String password, String cardType, double openMoney) {
        card = new Card(userId, userPid, userName, userType, password, cardType, openMoney);
        long cardId = cardRepository.save(card).getCardId();
        return cardId;
    }
}
