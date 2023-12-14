package com.example.banksys.businesslogiclayer.service;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.businesslogiclayer.exception.VipOpenMoneyNotEnoughException;
import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.businesslogiclayer.useraccount.BaseCurrentAccountRight;
import com.example.banksys.businesslogiclayer.useraccount.BaseFixedAccountRight;
import com.example.banksys.businesslogiclayer.useraccount.PersonalUserAccount;
import com.example.banksys.dataaccesslayer.PersonalCardRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.*;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.log.AccountLog;
import com.example.banksys.presentationlayer.form.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 个人用户Service
 */
@Service
public class PersonalService extends BaseUserService implements UserService {

    /**
     * VIP用户开户金额门限
     */
    public static final double VIP_OPEN_THRESHOLD = 1000000;
    private PersonalCardRepository personalCardRepository;
    private UserRepository userRepository;

    public PersonalService(PersonalCardRepository personalCardRepository, UserRepository userRepository) {
        this.personalCardRepository = personalCardRepository;
        this.userRepository = userRepository;
    }

    /**
     * 个人用户开户
     * @param personalUserAccount 个人用户账户
     * @param passwordEncoder 密码加密器
     * @param userRepository
     * @param openForm 开户表单
     * @return 所新开账户的账户ID
     * @throws VipOpenMoneyNotEnoughException vip开户金额不足异常
     */
    @Transactional
    public Long openAccount(PersonalUserAccount personalUserAccount,
                            PasswordEncoder passwordEncoder,
                            UserRepository userRepository,
                            OpenForm openForm
                            ) throws VipOpenMoneyNotEnoughException {

        // 检查开户人名下所有账户的开户金额是否满足VIP开户金额门限
        if (openForm.getUserType().equals(Card.UserType.VIP)) {
            checkOpenMoney(openForm.getOpenMoney(), openForm.getUserPid());
        }
        Employee employee = personalUserAccount.getEmployee();
        String encoded = passwordEncoder.encode(openForm.getPassword());
        // 保存user
        User user = userRepository.save(new User(openForm.getUserPid(), openForm.getUserName(), openForm.getUserType(),encoded));
        personalUserAccount.setUser(user);
        // 开户
        Long cardId = personalUserAccount.openAccount(
                user.getUserId(),
                openForm.getUserPid(),
                openForm.getUserName(),
                openForm.getUserType(),
                encoded,
                null,
                openForm.getCardType(),
                openForm.getOpenMoney(),
                employee);
        return user.getUserId();
    }

    /**
     * 检查开户人名下所有账户的开户金额是否满足VIP开户金额门限，若满足，则其名下所有账户升级为VIP账户
     * @param openMoney 开户金额
     * @param userPid 用户身份证号
     * @throws VipOpenMoneyNotEnoughException vip开户金额不足异常
     */
    private void checkOpenMoney(double openMoney, String userPid) throws VipOpenMoneyNotEnoughException {
        Optional<List<PersonalCard>> personalCardsByUserPid = personalCardRepository.findPersonalCardsByUserPid(userPid);
        double sum = openMoney;
        if (personalCardsByUserPid.isPresent()) {
            for (PersonalCard personalCard : personalCardsByUserPid.get()) {
                sum += personalCard.getOpenMoney();
            }
        }
        if (sum < VIP_OPEN_THRESHOLD) {
            throw new VipOpenMoneyNotEnoughException("vip开户金额不能小于" + VIP_OPEN_THRESHOLD + "元！");
        }

        List<Long> userIds = new ArrayList<>();
        personalCardsByUserPid.get().forEach(personalCard -> {
            if (!personalCard.getUserType().equals(Card.UserType.VIP)){
                personalCard.setUserType(Card.UserType.VIP);
                userIds.add(personalCard.getUserId());
            }
        });
        Iterable<User> allById = userRepository.findAllById(userIds);
        allById.forEach(user -> user.setUserType(Card.UserType.VIP));
        userRepository.saveAll(allById); // 更新用户表
        personalCardRepository.saveAll(personalCardsByUserPid.get()); // 更新卡表
    }

    /**
     * 活期存款
     * @param accountRight  活期账户权力接口
     * @param form 活期存款表单
     * @return 存款后余额
     */
    @Override
    @Transactional
    public double depositCurrent(BaseCurrentAccountRight accountRight, DepositCurrentForm form) {
        return accountRight.deposit(form.getMoney());
    }

    /**
     * 定期存款
     * @param accountRight  定期账户权力接口
     * @param form 定期存款表单
     * @return 存款后余额
     */
    @Override
    @Transactional
    public double depositFixed(BaseFixedAccountRight accountRight, DepositFixedForm form) {
        return accountRight.deposit(form.getMoney(), form.getDepositDays());
    }

    /**
     * 取款
     * @param account 用户基类
     * @param form 取款表单
     * @return 取款后余额
     * @throws EnterpriseWithdrawBalanceNotEnoughException 企业用户取款后余额不足门限异常
     * @throws WithdrawException 取款后余额为负异常
     */
    @Override
    @Transactional
    public double withdraw(BaseAccount account, WithdrawForm form) throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException {
        return account.withdraw(form.getMoney());
    }

    /**
     * 查询余额
     * @param account 用户基类
     * @return 返回”可取余额/总余额：XX/XX元“
     */
    @Override
    @Transactional
    public String queryBalance(BaseAccount account) {
        return account.queryBalance();
    }

    /**
     * 查询日志
     * @param account 用户基类
     * @return 用户查询的日志
     */
    @Override
    @Transactional
    public List<AccountLog> queryQueryLogs(BaseAccount account) {
        return account.queryQueryLogs();
    }

    /**
     * 查询交易
     * @param account 用户基类
     * @return 交易明细
     */
    @Override
    @Transactional
    public List<Trade> queryTrades(BaseAccount account) {
        return account.queryTrades();
    }

    /**
     * 转账
     * @param account 用户基类
     * @param transferForm 转账表单
     * @return 转账后转出账户的余额
     * @throws EnterpriseWithdrawBalanceNotEnoughException 企业用户取款后余额不足门限异常
     * @throws UntransferableException 不可转账异常
     * @throws WithdrawException 取款后余额为负异常
     */
    @Override
    @Transactional
    public double transfer(BaseAccount account, TransferForm transferForm) throws EnterpriseWithdrawBalanceNotEnoughException, UntransferableException, WithdrawException {
        User toUser = userRepository.findById(transferForm.getToUserId()).get();
        Card toCard = toUser.getCard();
        return account.transferMoneyTo(toCard,transferForm.getMoney());
    }

    /**
     * 改密码
     * @param account 用户基类
     * @param passwordEncoder 密码加密器
     * @param changePasswordForm 改密码表单
     */
    @Override
    @Transactional
    public void changePassword(BaseAccount account, PasswordEncoder passwordEncoder, ChangePasswordForm changePasswordForm) {
        super.changePassword(account, passwordEncoder, changePasswordForm);
    }

    /**
     * 销户
     * @param account 用户基类
     * @return 销户时取出的所有余额
     */
    @Override
    @Transactional
    public double close(BaseAccount account) {
        return account.closeAccount();
    }

}
