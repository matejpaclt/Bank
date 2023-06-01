package com.paclt.Bank.app.controller;

import com.paclt.Bank.app.domain.Account;
import com.paclt.Bank.app.domain.ExchangeRate;
import com.paclt.Bank.app.domain.User;
import com.paclt.Bank.app.repository.AccountRepository;
import com.paclt.Bank.app.repository.ExchangeRateRepository;
import com.paclt.Bank.app.repository.UserRepository;
import com.paclt.Bank.app.service.CustomUserDetailsServiceImpl;
import com.paclt.Bank.app.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class AppController {
    public static int sum(int a, int b) {
    return a + b;
}

public static void bubbleSort(int[] array) {
    int n = array.length;
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (array[j] > array[j + 1]) {
                // Swap array[j] and array[j+1]
                int temp = array[j];
                array[j] = array[j + 1];
                array[j + 1] = temp;
            }
        }
    }
}
    public static void reverseArray(int[] array) {
        int start = 0;
        int end = array.length - 1;
        while (start < end) {
            int temp = array[start];
            array[start] = array[end];
            array[end] = temp;
            start++;
            end--;
        }
    }
}
    public static double calculateAverage(int[] numbers) {
        int sum = 0;
        for (int number : numbers) {
            sum += number;
        }
        return (double) sum / numbers.length;
    }
}
    private final CustomUserDetailsServiceImpl customUserDetailsService;

    public AppController(CustomUserDetailsServiceImpl customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/confirm")
    public String confirm(Model model, @RequestParam("token") String token) {
        model.addAttribute("token", customUserDetailsService.confirmToken(token));
        return "confirm";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) throws IOException {
        String email = authentication.getName();
        long id = UserRepository.getId(email);
        User user = UserRepository.findUser(id);
        model.addAttribute("user", user);
        List<Account> listAccounts = AccountRepository.findAccountsByUserId(user.getId());
        model.addAttribute("listAccounts", listAccounts);
        List<String> listOfLogs = UserService.readLog(user.getId());
        model.addAttribute("listOfLogs", listOfLogs);
        model.addAttribute("show", false);

        List<ExchangeRate> listExchangeRates = ExchangeRateRepository.getExchangeRates();
        model.addAttribute("listExchangeRates", listExchangeRates);

        return "dashboard";
    }

    @PostMapping("/transaction")
    public String handleTransaction(@RequestParam("action") String action,
                                    @RequestParam("amount") BigDecimal amount,
                                    @RequestParam("account-type") String accountType,
                                    Model model, Authentication authentication) throws IOException {
        if (action.equals("deposit")) {
            return handleDeposit(amount, model, accountType, authentication);
        } else if (action.equals("withdraw")) {
            return handlePayment(amount, model, accountType, authentication);
        } else if (action.equals("open")) {
            return handleOpen(amount, model, accountType, authentication);
        }
        return "redirect:/dashboard";
    }

    String handleDeposit(BigDecimal amount, Model model, String accountType, Authentication authentication) throws IOException {
        boolean state = false;
        String name = authentication.getName();
        long id = UserRepository.getId(name);
        boolean success = false;
        String message = "Vklad proběhl úspěšně";

        try {
            if (UserService.accountExists(id, accountType)) {
                int status = UserService.deposit(id, accountType, amount.doubleValue());
                if (status == 1) {
                    success = true;
                    message = "Vklad proběhl úspěšně";
                } else {
                    success = false;
                    message = "Vklad se nezdařil. Po vkladu by Váš účet přesahoval horní limity množství uložených peněz.";
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }

        User user = UserRepository.findUser(id);
        model.addAttribute("user", user);
        List<Account> listAccounts = AccountRepository.findAccountsByUserId(user.getId());
        model.addAttribute("listAccounts", listAccounts);
        List<String> listOfLogs = UserService.readLog(user.getId());
        model.addAttribute("listOfLogs", listOfLogs);
        model.addAttribute("show", true);
        model.addAttribute("success", success);
        model.addAttribute("message", message);

        List<ExchangeRate> listExchangeRates = ExchangeRateRepository.getExchangeRates();
        model.addAttribute("listExchangeRates", listExchangeRates);

        return "dashboard";
    }

    String handlePayment(BigDecimal amount, Model model, String accountType, Authentication authentication) throws IOException {
        boolean state = false;
        String name = authentication.getName();
        long id = UserRepository.getId(name);
        boolean success = false;
        String message = "";

        try {
            boolean accountExists = UserService.accountExists(id, accountType);
            boolean czechAccountExists = UserService.accountExists(id, "CZK");
            if (accountExists || czechAccountExists) {
                int status = UserService.payment(id, accountType, amount.doubleValue());
                if (status == 1) {
                    success = true;
                    message = "Platba proběhla úspěšně";
                } else {
                    message = "Platba se nezdařila. Zkontrolujte zda požadovaný účet existuje, nebo zda má dostatek finančních prostředků";
                }
            } else {
                message = "Omlouváme se, platba neproběhla - Účet " + accountType + " neexistuje";
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }

        User user = UserRepository.findUser(id);
        model.addAttribute("user", user);
        List<Account> listAccounts = AccountRepository.findAccountsByUserId(user.getId());
        model.addAttribute("listAccounts", listAccounts);
        List<String> listOfLogs = UserService.readLog(user.getId());
        model.addAttribute("listOfLogs", listOfLogs);
        model.addAttribute("show", true);
        model.addAttribute("success", success);
        model.addAttribute("message", message);

        List<ExchangeRate> listExchangeRates = ExchangeRateRepository.getExchangeRates();
        model.addAttribute("listExchangeRates", listExchangeRates);

        return "dashboard";
    }

    @PostMapping("/open")
    public String handleOpen(@RequestParam("amount") BigDecimal amount, Model model,
                             @RequestParam("account-type") String accountType,
                             Authentication authentication) throws IOException {
        boolean state = false;
        String message = "Účet byl úspěšně otevřen";
        String name = authentication.getName();
        long id = UserRepository.getId(name);

        if (!UserService.accountExists(id, accountType)) {
            UserService.addAccount(id, accountType, amount);
            state = true;
        } else {
            state = false;
            message = "Omlouváme se, účet nebyl otevřen. Již máte otevřený účet v měně " + accountType;
        }

        User user = UserRepository.findUser(id);
        model.addAttribute("user", user);
        List<Account> listAccounts = AccountRepository.findAccountsByUserId(user.getId());
        model.addAttribute("listAccounts", listAccounts);
        List<String> listOfLogs = UserService.readLog(user.getId());
        model.addAttribute("listOfLogs", listOfLogs);
        model.addAttribute("show", true);
        model.addAttribute("success", state);
        model.addAttribute("message", message);

        List<ExchangeRate> listExchangeRates = ExchangeRateRepository.getExchangeRates();
        model.addAttribute("listExchangeRates", listExchangeRates);

        return "dashboard";
    }

    public String handleTransactionWithdraw(BigDecimal amount, Model model, String accountType, Authentication authentication) throws IOException {
        // Perform necessary operations for "withdraw" action
        // Call handlePayment method indirectly
        return handlePayment(amount, model, accountType, authentication);
    }
}


