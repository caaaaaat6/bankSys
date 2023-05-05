package com.example.banksys.model;

public class OrdinaryCurrentUserAccount extends OrdinaryUserAccount implements CurrentUserAccountRight {


    @Override
    public long openAccount(long userId, String userPid, String userName, String userType, String password, String cardType, double openMoney) {
        card = new Card(userId, userPid, userName, userType, password, cardType, openMoney);
        long cardId = cardRepository.save(card).getCardId();
        return cardId;
    } 

    @Override
    public void save() {

    }
}
