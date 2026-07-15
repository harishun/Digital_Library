package services;

import models.User;
import models.Member;
import models.Transaction;

import java.util.ArrayList;

public class NotificationService {

    public static void checkOverdueNotifications(User user, ArrayList<Transaction> transactions) {
        if (user == null || !user.getRole().equals("Member")) {
            return;
        }

        Member member = (Member) user;
        boolean hasOverdue = false;

        System.out.println("\n================= SYSTEM NOTIFICATIONS =================");
        
        // 1. Check for Overdue Fines
        if (member.getBalance() > 0.0) {
            System.out.println("[ALERT] You have outstanding fines of $" + String.format("%.2f", member.getBalance()) + ".");
            System.out.println("        Please settle your fines to restore borrowing privileges.");
            hasOverdue = true;
        }

        // 2. Check for Overdue Books
        for (Transaction t : transactions) {
            if (t.getMemberId().equals(member.getUserId()) && t.getTransactionStatus().equals("Borrowed")) {
                long overdueDays = t.calculateOverdueDays();
                if (overdueDays > 0) {
                    System.out.println("[WARNING] OVERDUE BOOK DETECTED:");
                    System.out.println("          Book: \"" + t.getBookTitle() + "\" (ISBN: " + t.getBookIsbn() + ")");
                    System.out.println("          Was due on: " + t.getDueDate().toString());
                    System.out.println("          Days Overdue: " + overdueDays);
                    hasOverdue = true;
                }
            }
        }

        if (!hasOverdue) {
            System.out.println("No notifications. Your account is in good standing.");
        }
        System.out.println("========================================================\n");
    }

    public static void displaySystemOverdueAlerts(ArrayList<Transaction> transactions) {
        System.out.println("\n=== System Overdue Activity Monitor ===");
        boolean found = false;
        for (Transaction t : transactions) {
            if (t.getTransactionStatus().equals("Borrowed")) {
                long overdueDays = t.calculateOverdueDays();
                if (overdueDays > 0) {
                    System.out.println("\n[SYSTEM ALERT] Overdue Book Record:");
                    System.out.println("  Transaction ID : " + t.getTransactionId());
                    System.out.println("  Member ID      : " + t.getMemberId());
                    System.out.println("  Book           : " + t.getBookTitle() + " (ISBN: " + t.getBookIsbn() + ")");
                    System.out.println("  Due Date       : " + t.getDueDate().toString());
                    System.out.println("  Overdue Duration: " + overdueDays + " days");
                    System.out.println("  Estimated Fine : $" + String.format("%.2f", overdueDays * 1.0));
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("No overdue books are currently tracked in the system.");
        }
        System.out.println("========================================\n");
    }
}
