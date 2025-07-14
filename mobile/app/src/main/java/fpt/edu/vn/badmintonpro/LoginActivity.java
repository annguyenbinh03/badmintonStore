package fpt.edu.vn.badmintonpro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import fpt.edu.vn.badmintonpro.api.ApiClient;
import fpt.edu.vn.badmintonpro.api.ApiService;
import fpt.edu.vn.badmintonpro.model.LoginRequest;
import fpt.edu.vn.badmintonpro.model.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    // Thay đổi EditText thành TextInputEditText để khớp với XML
    private TextInputEditText edtEmail, edtPassword;
    private Button btnLogin;
    // Thêm các TextView mới
    private TextView tvForgotPassword, tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ ViewD
        initViews();

        // Bắt sự kiện
        setupClickListeners();
    }

    /**
     * Phương thức để ánh xạ các view từ layout XML.
     */
    private void initViews() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
    }

    /**
     * Phương thức để thiết lập các sự kiện click cho các view.
     */
    private void setupClickListeners() {
        // Sự kiện đăng nhập
        btnLogin.setOnClickListener(v -> login());

        // Sự kiện cho nút "Quên mật khẩu"
        tvForgotPassword.setOnClickListener(v -> {
            // TODO: Xử lý logic chuyển sang màn hình Quên mật khẩu
            Toast.makeText(LoginActivity.this, "Chức năng Quên mật khẩu đang được phát triển!", Toast.LENGTH_SHORT).show();
            // Ví dụ:
            // Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            // startActivity(intent);
        });

        // Sự kiện cho nút "Đăng ký"
        tvSignUp.setOnClickListener(v -> {
            // TODO: Xử lý logic chuyển sang màn hình Đăng ký
            Toast.makeText(LoginActivity.this, "Chuyển đến màn hình Đăng ký...", Toast.LENGTH_SHORT).show();
            // Ví dụ:
            // Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            // startActivity(intent);
        });
    }

    /**
     * Phương thức xử lý logic đăng nhập.
     */
    private void login() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gửi yêu cầu đăng nhập
        LoginRequest request = new LoginRequest(email, password);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.isSuccess()) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                        // Chuyển sang MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Đóng LoginActivity để người dùng không quay lại được bằng nút back
                    } else {
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Xử lý các lỗi HTTP khác nhau, ví dụ: 401 Unauthorized, 404 Not Found
                    String errorMessage = "Đăng nhập thất bại. Vui lòng thử lại.";
                    if (response.code() == 401) {
                        errorMessage = "Sai email hoặc mật khẩu.";
                    }
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e("LOGIN_FAIL", "Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("LOGIN_ERROR", "Throwable: ", t);
            }
        });
    }
}