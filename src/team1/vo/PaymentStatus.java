package team1.vo;

/*
*
*
* */
public enum PaymentStatus {
    PENDING("처리중"),
    APPROVE("승인됨"),
    CANCEL("취소됨");

    PaymentStatus(String name) {
    }
}
