package com.example.trackmydooit;

public class TransactionModel {


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;
    private String expenseId;
    private String note;
    private String ExpenseCategory;
    private String incomeCategory;
    private String WalletCategory;
    private String type;
    private String amount;
    private String uid;
    private String time;


    public TransactionModel(){

    }

    public TransactionModel(String expenseId, String type, String note, String expenseCategory, String incomeCategory, String walletCategory, String amount, String time, String uid) {
        this.type = type;
        this.expenseId = expenseId;
        this.note = note;
        this.ExpenseCategory = expenseCategory;
        this.incomeCategory = incomeCategory;
        this.WalletCategory = walletCategory;
        this.amount = amount;
        this.time = time;
        this.uid = uid;
    }

    public String getIncomeCategory() {
        return incomeCategory;
    }

    public void setIncomeCategory(String incomeCategory) {
        this.incomeCategory = incomeCategory;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getExpenseCategory() {
        return ExpenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        ExpenseCategory = expenseCategory;
    }

    public String getWalletCategory() {
        return WalletCategory;
    }

    public void setWalletCategory(String walletCategory) {
        WalletCategory = walletCategory;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
