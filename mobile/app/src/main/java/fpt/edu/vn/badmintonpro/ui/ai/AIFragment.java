package fpt.edu.vn.badmintonpro.ui.ai;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import fpt.edu.vn.badmintonpro.databinding.FragmentAiBinding;

public class AIFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private FragmentAiBinding binding;
    private AIViewModel aiViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        aiViewModel = new ViewModelProvider(this).get(AIViewModel.class);
        binding = FragmentAiBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Request runtime permissions for camera and audio
        requestPermissionsIfNeeded();

        setupWebView(binding.webviewAi);
        return root;
    }

    private void requestPermissionsIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA
            };
            boolean allGranted = true;
            for (String permission : permissions) {
                if (requireActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (!allGranted) {
                ActivityCompat.requestPermissions(requireActivity(), permissions, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("AIFragment", "Permissions granted");
                // Reload WebView if needed
                setupWebView(binding.webviewAi);
            } else {
                Log.e("AIFragment", "Permissions denied");
            }
        }
    }

    private void setupWebView(WebView webView) {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.d("AIFragment", "Permission request: " + request.getResources());
                requireActivity().runOnUiThread(() -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (requireActivity().checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                                requireActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            request.grant(request.getResources());
                        } else {
                            request.deny();
                            Log.e("AIFragment", "Permission not granted, denying request");
                        }
                    }
                });
            }
        });

        webView.setWebViewClient(new WebViewClient());

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setAllowFileAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        // Load the ElevenLabs AI URL
        String url = "https://elevenlabs.io/app/talk-to?agent_id=HOHyaemgMsoFc0eUole8";
        webView.loadUrl(url);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}