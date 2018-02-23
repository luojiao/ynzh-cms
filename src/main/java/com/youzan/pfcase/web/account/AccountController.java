package com.youzan.pfcase.web.account;

import com.youzan.pfcase.domain.Account;
import com.youzan.pfcase.service.AccountService;
import com.youzan.pfcase.web.account.AccountVo.NewAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.groups.Default;



/**
 * Created by sunjun on 16/8/8.
 */
@Controller
@RequestMapping("account")
public class AccountController {

    @Autowired
    protected AccountHelper accountHelper;

    @Autowired
    protected AccountService accountService;

    @Autowired
    protected PasswordEqualsValidator passwordEqualsValidator;

    @InitBinder("accountForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(passwordEqualsValidator);
    }

    @ModelAttribute
    public AccountVo setUpForm() {
        return new AccountVo();
    }



    @RequestMapping(value = "signonForm", method = RequestMethod.GET)
    public String signonFormGet() {
        return "account/SignonForm";
    }
    @RequestMapping(value = "signonForm", method = RequestMethod.POST)
    public String signonForm(AccountVo accountVo, ModelAndView modelAndView) {
        if(accountVo == null){
            return "account/SignonForm";
        }
        Account account = accountService.getAccount(accountVo.getUsername());
        if (account == null){
            modelAndView.addObject("error","账号不存在");
        }else if (account.getPassword().equals(accountVo.getPassword())){
            modelAndView.addObject("error","密码差点就对了");
        }
        return "account/SignonForm";
    }

    @RequestMapping("newAccountForm")
    public String newAccountForm() {
        return "account/NewAccountForm";
    }

    @RequestMapping("newAccount")
    public String newAccount(@Validated({ NewAccount.class, Default.class }) AccountVo form, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "account/NewAccountForm";
        }
        if (accountService.getAccount(form.getUsername()) != null) {
            model.addAttribute("duplicatedUsers", "用户名已存在");
            return "account/NewAccountForm";
        }
        accountHelper.newAccount(form);
        return "redirect:/account/signonForm";
    }

}
