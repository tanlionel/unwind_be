package com.capstone.unwind.config;

public class EmailMessageConfig {

    // Subject cho email
    public static final String CREATE_EXCHANGE_POSTING_SUBJECT = "Bài đăng trao đổi của bạn đã được tạo thành công";
    public static final String CREATE_RENTAL_POSTING_SUBJECT = "Bài đăng cho thuê của bạn đã được tạo thành công";
    public static final String APPROVAL_EXCHANGE_POSTING_SUBJECT = "Bài đăng trao đổi của bạn đã được duyệt thành công";
    public static final String APPROVAL_RENTAL_POSTING_SUBJECT = "Bài đăng cho thuê của bạn đã được duyệt thành công";
    public static final String REJECT_EXCHANGE_POSTING_SUBJECT = "Bài đăng trao đổi của bạn bị từ chối";
    public static final String APPROVAL_EXCHANGE_REQUEST_SUBJECT = "Yêu cầu trao đổi của bạn đã được chấp nhận";
    public static final String REJECT_EXCHANGE_REQUEST_SUBJECT = "Yêu cầu trao đổi của bạn bị từ chối";
    public static final String REJECT_RENTAL_POSTING_SUBJECT = "Bài đăng cho thuê của bạn bị từ chối";
    public static final String REJECT_RENTAL_BOOKING_SUBJECT = "Bài đăng cho thuê của bạn bị hủy booking";
    public static final String FORM_CONTACT_RENTAL_PACKAGE_01_SUBJECT = "Bài đăng cho thuê gói cơ bản của bạn vừa được yêu cầu thuê";
    public static final String REJECT_RENTAL_BOOKING_CONTENT =
            "Rất tiếc,Bài đăng cho thuê của bạn đã bị hủy booking. "
                    + "Nếu bạn có bất kỳ câu hỏi nào hoặc cần hỗ trợ, vui lòng liên hệ với chúng tôi. "
                    + "Chúng tôi sẽ rất vui được giúp đỡ bạn. Cảm ơn bạn đã sử dụng nền tảng của chúng tôi!";
    public static final String TIMESHARE_COMPANY_CREATION_SUBJECT = "Tài khoản Timeshare Company của bạn đã được tạo thành công";
    public static final String TIMESHARE_COMPANY_CREATION_CONTENT = "Tài khoản của bạn tại Unwind đã được tạo thành công. "
            + "Bạn có thể bắt đầu sử dụng các dịch vụ của hệ thống. "
            + "Nếu có bất kỳ câu hỏi nào, vui lòng liên hệ với đội ngũ hỗ trợ của chúng tôi.";
    public static final String RESORT_CREATION_SUBJECT = "Resort của bạn đã được tạo thành công";

    public static final String RESORT_CREATION_CONTENT = " Resort của bạn đã được tạo thành công trên nền tảng của chúng tôi. "
            + "Nếu bạn cần sự hỗ trợ, đội ngũ của chúng tôi luôn sẵn sàng hỗ trợ bạn.";

    // Content cho email
    public static final String CREATE_EXCHANGE_POSTING_CONTENT =
            "Bài đăng trao đổi của bạn đã được tạo thành công và đang trong quá trình xử lý. ";
    public static final String CREATE_RENTAL_POSTING_CONTENT =
            "Bài đăng cho thuê của bạn đã được tạo thành công và đang trong quá trình xử lý. ";
    public static final String APPROVAL_EXCHANGE_POSTING_CONTENT =
            "Bài đăng trao đổi của bạn đã được xét duyệt thành công và hiện đã sẵn sàng để hoạt động. "
                    + "Bạn có thể theo dõi tình trạng bài đăng hoặc thực hiện các cập nhật nếu cần. "
                    + "Cảm ơn bạn đã sử dụng nền tảng của chúng tôi. Chúc bạn có trải nghiệm tuyệt vời!";
    public static final String APPROVAL_EXCHANGE_REQUEST_CONTENT =
            "Yêu cầu trao đổi của bạn đã được chấp nhận. "
                    + "Cảm ơn bạn đã sử dụng nền tảng của chúng tôi. Chúc bạn có trải nghiệm tuyệt vời!";
    public static final String APPROVAL_RENTAL_POSTING_CONTENT =
            "Bài đăng cho thuê của bạn đã được xét duyệt thành công và hiện đã sẵn sàng để hoạt động. "
                    + "Bạn có thể theo dõi tình trạng bài đăng hoặc thực hiện các cập nhật nếu cần. "
                    + "Cảm ơn bạn đã sử dụng nền tảng của chúng tôi. Chúc bạn có trải nghiệm tuyệt vời!";
    public static final String REJECT_EXCHANGE_POSTING_CONTENT =
            "Rất tiếc, bài đăng trao đổi của bạn không được duyệt. "
                    + "Mặc dù bài đăng bị từ chối, nhưng chi phí đã được trừ theo gói dịch vụ bạn đã chọn. "
                    + "Vui lòng kiểm tra lại các thông tin và yêu cầu bài đăng, sau đó thử gửi lại. "
                    + "Nếu bạn có bất kỳ câu hỏi nào hoặc cần hỗ trợ, vui lòng liên hệ với chúng tôi. "
                    + "Chúng tôi sẽ rất vui được giúp đỡ bạn. Cảm ơn bạn đã sử dụng nền tảng của chúng tôi!";
    public static final String REJECT_EXCHANGE_REQUEST_CONTENT =
            "Rất tiếc, yêu cầu trao đổi của bạn bị từ chối. "
                    + "Nếu bạn có bất kỳ câu hỏi nào hoặc cần hỗ trợ, vui lòng liên hệ với chúng tôi. "
                    + "Chúng tôi sẽ rất vui được giúp đỡ bạn. Cảm ơn bạn đã sử dụng nền tảng của chúng tôi!";
    public static final String REJECT_RENTAL_POSTING_CONTENT =
            "Rất tiếc, bài đăng trao đổi của bạn không được duyệt. "
                    + "Mặc dù bài đăng bị từ chối, nhưng chi phí đã được trừ theo gói dịch vụ bạn đã chọn. "
                    + "Vui lòng kiểm tra lại các thông tin và yêu cầu bài đăng, sau đó thử gửi lại. "
                    + "Nếu bạn có bất kỳ câu hỏi nào hoặc cần hỗ trợ, vui lòng liên hệ với chúng tôi. "
                    + "Chúng tôi sẽ rất vui được giúp đỡ bạn. Cảm ơn bạn đã sử dụng nền tảng của chúng tôi!";
    public static final String FORM_CONTACT_RENTAL_PACKAGE_01_CONTENT =
           "Bạn vừa có yêu cầu thuê phòng từ unwind với nội dung: ";
}