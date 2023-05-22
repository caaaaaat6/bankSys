package com.example.banksys.businesslogiclayer.service;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.businesslogiclayer.useraccount.BaseCurrentAccountRight;
import com.example.banksys.businesslogiclayer.useraccount.BaseFixedAccountRight;
import com.example.banksys.businesslogiclayer.useraccount.EnterpriseUserAccount;
import com.example.banksys.dataaccesslayer.EnterpriseCardRepository;
import com.example.banksys.dataaccesslayer.EnterpriseRepository;
import com.example.banksys.dataaccesslayer.EnterpriseUserRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.*;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.log.AccountLog;
import com.example.banksys.presentationlayer.form.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 企业Service
 */
@Service
public class EnterpriseService extends BaseUserService implements UserService {

    private EnterpriseCardRepository enterpriseCardRepository;
    private EnterpriseUserRepository enterpriseUserRepository;
    private EnterpriseRepository enterpriseRepository;

    public EnterpriseService(EnterpriseCardRepository enterpriseCardRepository, EnterpriseUserRepository enterpriseUserRepository, EnterpriseRepository enterpriseRepository) {
        this.enterpriseCardRepository = enterpriseCardRepository;
        this.enterpriseUserRepository = enterpriseUserRepository;
        this.enterpriseRepository = enterpriseRepository;
    }

    /**
     * 企业开户
     * @param enterpriseUserAccount 企业用户账户基类
     * @param passwordEncoder 密码加密器
     * @param enterpriseOpenForm 企业账户开户表单
     * @return 所新开企业账户的账户ID
     */
    @Transactional
    public Long openAccount(EnterpriseUserAccount enterpriseUserAccount,
                            PasswordEncoder passwordEncoder,
                            EnterpriseOpenForm enterpriseOpenForm
    )  {
        Employee employee = enterpriseUserAccount.getEmployee();
        String encoded = passwordEncoder.encode(enterpriseOpenForm.getPassword());
        Enterprise enterprise = enterpriseRepository.findById(enterpriseOpenForm.getEnterpriseId()).get();
        EnterpriseUser enterpriseUser = enterpriseUserRepository.save(new EnterpriseUser(
                enterpriseOpenForm.getUserPid(),
                enterpriseOpenForm.getUserName(),
                encoded,
                enterprise
                ));
        enterpriseUserAccount.setEnterpriseUser(enterpriseUser);
        enterpriseUserAccount.setEnterprise(enterprise);
        enterpriseUserAccount.openEnterpriseAccount(
                enterpriseUser.getUserId(),
                enterpriseOpenForm.getUserPid(),
                enterpriseOpenForm.getUserName(),
                encoded,
                null,
                enterpriseOpenForm.getCardType(),
                enterpriseOpenForm.getOpenMoney(),
                employee);
        return enterpriseUser.getUserId();
    }

    @Override
    @Transactional
    public double depositCurrent(BaseCurrentAccountRight accountRight, DepositCurrentForm depositCurrentForm) {
        return accountRight.deposit(depositCurrentForm.getMoney());
    }
    @Override
    @Transactional
    public double depositFixed(BaseFixedAccountRight accountRight, DepositFixedForm depositFixedForm) {
        return accountRight.deposit(depositFixedForm.getMoney(), depositFixedForm.getDepositDays());
    }

    @Override
    @Transactional
    public double withdraw(BaseAccount account, WithdrawForm form) throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException {
        return account.withdraw(form.getMoney());
    }

    @Override
    @Transactional
    public String queryBalance(BaseAccount account) {
        return account.queryBalance();
    }

    @Override
    @Transactional
    public List<AccountLog> queryQueryLogs(BaseAccount account) {
        return account.queryQueryLogs();
    }

    @Override
    @Transactional
    public List<Trade> queryTrades(BaseAccount account) {
        return account.queryTrades();
    }

    @Override
    @Transactional
    public double transfer(BaseAccount account, TransferForm transferForm) throws EnterpriseWithdrawBalanceNotEnoughException, UntransferableException, WithdrawException {
        User toUser = enterpriseUserRepository.findById(transferForm.getToUserId()).get();
        Card toCard = toUser.getCard();
        return account.transferMoneyTo(toCard,transferForm.getMoney());
    }

    @Override
    @Transactional
    public void changePassword(BaseAccount account, PasswordEncoder passwordEncoder, ChangePasswordForm changePasswordForm) {
        super.changePassword(account, passwordEncoder, changePasswordForm);
    }

    @Override
    @Transactional
    public double close(BaseAccount account) {
        return account.closeAccount();
    }

    /**
     * super查询该企业下所有用户
     * @param account 企业用户账户
     * @return 该企业下所有用户
     */
    @Transactional
    public List<EnterpriseUser> getEnterpriseUsers(EnterpriseUserAccount account) {
        List<EnterpriseUser> enterpriseUserList = account.getEnterpriseUser().getEnterprise().getEnterpriseUserList();
        return  enterpriseUserList;
    }

    /**
     * 删除企业用户
     * @param userRepository
     * @param form 被选中用户账户ID的表单
     */
    @Transactional
    public void deleteEnterpriseUser(UserRepository userRepository, SelectedUserIdForm form) {
        userRepository.deleteAllById(form.getSelectedUserId());
    }

    /**
     * super添加用户账户
     * @param account 企业用户账户
     * @param enterpriseUserRepository
     * @param form 添加企业用户表单
     * @param encoder
     * @return 所新建企业用户账户ID
     */
    @Transactional
    public Long addEnterpriseUser(EnterpriseUserAccount account, EnterpriseUserRepository enterpriseUserRepository, AddEnterpriseUserForm form, PasswordEncoder encoder) {
        EnterpriseUser enterpriseUser = new EnterpriseUser(form.getUserPid(), form.getUserName(), Card.UserType.ENTERPRISE, encoder.encode(form.getPassword()), EnterpriseUser.RightType.USER, account.getEnterprise(), account.getEnterpriseCard());
        EnterpriseUser save = enterpriseUserRepository.save(enterpriseUser);
        return save.getUserId();
    }

    /**
     * 添加企业
     * @param enterpriseRepository
     * @param form 添加企业表单
     */
    @Transactional
    public void addEnterprise(EnterpriseRepository enterpriseRepository, AddEnterpriseForm form) {
        Enterprise enterprise = new Enterprise(form.getEnterpriseName());
        enterpriseRepository.save(enterprise);
    }
}
