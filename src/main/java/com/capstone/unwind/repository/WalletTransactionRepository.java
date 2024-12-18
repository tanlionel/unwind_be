package com.capstone.unwind.repository;

import com.capstone.unwind.entity.WalletTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, UUID> {
    Optional<WalletTransaction> findById(UUID id);
    Page<WalletTransaction> findAllByWalletId(Integer walletId, Pageable pageable);
    @Query("SELECT wt FROM WalletTransaction wt WHERE wt.wallet.id = :walletId  AND wt.money > 0")
    Page<WalletTransaction> findAllMoneyReceived(@Param("walletId") Integer walletId, Pageable pageable);
    @Query("SELECT wt FROM WalletTransaction wt WHERE wt.wallet.id = :walletId  AND wt.money < 0")
    Page<WalletTransaction> findAllMoneySpent(@Param("walletId") Integer walletId, Pageable pageable);
    @Query("SELECT FUNCTION('MONTH', wt.createdAt) AS month, " +
            "       FUNCTION('YEAR', wt.createdAt) AS year, " +
            "       SUM(wt.money) AS totalMoney " +
            "FROM WalletTransaction wt " +
            "WHERE wt.wallet.timeshareCompany.id = :timeshareCompanyId " +
            "AND wt.money > 0 " +
            "AND wt.createdAt >= :startDate " +
            "GROUP BY FUNCTION('YEAR', wt.createdAt), FUNCTION('MONTH', wt.createdAt) " +
            "ORDER BY FUNCTION('YEAR', wt.createdAt) ASC, FUNCTION('MONTH', wt.createdAt) ASC")
    List<Object[]> findMonthlyMoneyReceived(@Param("timeshareCompanyId") Integer timeshareCompanyId,
                                            @Param("startDate") LocalDateTime startDate);


    @Query("SELECT SUM(wt.money) FROM WalletTransaction wt WHERE wt.wallet.id = :walletId AND wt.money > 0")
    Double sumMoneyByCustomerIdAndMoneyGreaterThan(@Param("walletId") Integer walletId);

    @Query("SELECT SUM(wt.money) FROM WalletTransaction wt WHERE wt.wallet.id = :walletId AND wt.money < 0")
    Double sumMoneyByCustomerIdAndMoneyLessThan(@Param("walletId") Integer walletId);

    @Query("SELECT SUM(wt.money) " +
            "FROM WalletTransaction wt " +
            "WHERE wt.wallet.id = :walletId " +
            "AND DATE(wt.createdAt) = :date " +
            "AND wt.money > 0")
    Double sumMoneyByCustomerIdAndDateAndMoneyGreaterThan(
            @Param("walletId") Integer walletId,
            @Param("date") LocalDate date);

    @Query("SELECT SUM(wt.money) " +
            "FROM WalletTransaction wt " +
            "WHERE wt.wallet.id = :walletId " +
            "AND DATE(wt.createdAt) = :date " +
            "AND wt.money < 0")
    Double sumMoneyByCustomerIdAndDateAndMoneyLessThan(
            @Param("walletId") Integer walletId,
            @Param("date") LocalDate date);


    @Query("SELECT COUNT(r) FROM WalletTransaction r WHERE r.transactionType = 'MEMBERSHIP'")
    Long getTotalMEMBERSGIP();
    @Query("SELECT COUNT(w) FROM WalletTransaction w WHERE DATE(w.createdAt) = :date AND w.transactionType = 'MEMBERSHIP'")
    Long countMembershipPackageByDateRange(@Param("date") LocalDate date);

    Page<WalletTransaction> findAll(Pageable pageable);
    Page<WalletTransaction> findAllByTransactionType(String walletTransaction, Pageable pageable);

    @Query("SELECT COALESCE(SUM(w.money), 0) FROM WalletTransaction w WHERE w.transactionType = 'MEMBERSHIP'")
    Float calculateTotalMembershipTransactions();

    @Query("SELECT COALESCE(SUM(w.money), 0) FROM WalletTransaction w WHERE w.transactionType = 'RENTALPOSTING'")
    Float calculateTotalRentalPostingTransactions();

    @Query("SELECT COALESCE(SUM(w.money), 0) FROM WalletTransaction w WHERE w.transactionType = 'EXCHANGEPOSTING'and w.money < 0")
    Float calculateTotalExchangePostingTransactions();

    @Query("SELECT COALESCE(SUM(w.money), 0) " +
            "FROM WalletTransaction w " +
            "WHERE w.money < 0 AND w.transactionType IN ('MEMBERSHIP', 'RENTALPOSTING', 'EXCHANGEPOSTING')")
    float calculateNormalMoneyTotal();
    @Query("SELECT COALESCE(SUM(w.money), 0) " +
            "FROM WalletTransaction w " +
            "WHERE w.money > 0 AND w.transactionType = 'RENTALPACKAGE04'")
    float calculatePackageFourMoneyTotal();



}