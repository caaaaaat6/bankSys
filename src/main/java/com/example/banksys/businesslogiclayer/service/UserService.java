package com.example.banksys.businesslogiclayer.service;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.businesslogiclayer.useraccount.BaseCurrentAccountRight;
import com.example.banksys.businesslogiclayer.useraccount.BaseFixedAccountRight;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.Trade;
import com.example.banksys.model.log.AccountLog;
import com.example.banksys.presentationlayer.form.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户Service
 */
public interface UserService {

    /**
     * 活期存款
     * @param baseCurrentAccountRight  活期账户权力接口
     * @param depositCurrentForm 活期存款表单
     * @return 存款后余额
     */
    @Transactional
    double depositCurrent(BaseCurrentAccountRight baseCurrentAccountRight, DepositCurrentForm depositCurrentForm);

    /**
     * 定期存款
     * @param baseFixedAccountRight  定期账户权力接口
     * @param depositFixedForm 定期存款表单
     * @return 存款后余额
     */
    @Transactional
    double depositFixed(BaseFixedAccountRight baseFixedAccountRight, DepositFixedForm depositFixedForm);

    /**
     * 取款
     * @param account 用户基类
     * @param form 取款表单
     * @return 取款后余额
     * @throws EnterpriseWithdrawBalanceNotEnoughException 企业用户取款后余额不足门限异常
     * @throws WithdrawException 取款后余额为负异常
     */
    @Transactional
    double withdraw(BaseAccount account, WithdrawForm form) throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException;

    /**
     * 查询余额
     * @param account 用户基类
     * @return 返回”可取余额/总余额：XX/XX元“
     */
    @Transactional
    String queryBalance(BaseAccount account);

    /**
     * 查询日志
     * @param account 用户基类
     * @return 用户查询的日志
     */
    @Transactional
    List<AccountLog> queryQueryLogs(BaseAccount account);

    /**
     * 查询交易
     * @param account 用户基类
     * @return 交易明细
     */
    @Transactional
    List<Trade> queryTrades(BaseAccount account);

    /**
     * 转账
     * @param account 用户基类
     * @param transferForm 转账表单
     * @return 转账后转出账户的余额
     * @throws EnterpriseWithdrawBalanceNotEnoughException 企业用户取款后余额不足门限异常
     * @throws UntransferableException 不可转账异常
     * @throws WithdrawException 取款后余额为负异常
     */
    @Transactional
    double transfer(BaseAccount account, TransferForm transferForm) throws EnterpriseWithdrawBalanceNotEnoughException, UntransferableException, WithdrawException;

    /**
     * 改密码
     * @param account 用户基类
     * @param passwordEncoder 密码加密器
     * @param changePasswordForm 改密码表单
     */
    @Transactional
    void changePassword(BaseAccount account, PasswordEncoder passwordEncoder, ChangePasswordForm changePasswordForm);

    /**
     * 销户
     * @param account 用户基类
     * @return 销户时取出的所有余额
     */
    @Transactional
    double close(BaseAccount account);


}
