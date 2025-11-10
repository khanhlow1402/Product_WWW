package fit.iuh.legiakhanh_tuan07.controller;


import fit.iuh.legiakhanh_tuan07.entities.Product;
import fit.iuh.legiakhanh_tuan07.reposities.ProductRepository;
import fit.iuh.legiakhanh_tuan07.services.GeminiService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final GeminiService geminiService;
    private final ProductRepository productRepository;

    public ChatController(GeminiService geminiService, ProductRepository productRepository) {
        this.geminiService = geminiService;
        this.productRepository = productRepository;
    }

    @PostMapping("/ask")
    public ResponseEntity<String> askGemini(@RequestBody PromptRequest request) {
        String prompt = request.getPrompt();

        // 🔍 Lấy danh sách sản phẩm hiện có trong DB
        List<Product> products = productRepository.findAll();

        // Tạo mô tả tổng quan về các sản phẩm để Gemini hiểu
        String context = products.stream()
                .limit(20) // giới hạn để tránh prompt quá dài
                .map(p -> String.format("- %s: giá %.0f VND, danh mục %s",
                        p.getName(),
                        p.getPrice(),
                        p.getCategory() != null ? p.getCategory().getName() : "Không có"))
                .collect(Collectors.joining("\n"));

        // Gộp prompt người dùng với context
        String finalPrompt = """
                Dưới đây là danh sách sản phẩm đang có:
                %s
                
                Dựa vào thông tin này, hãy gợi ý sản phẩm phù hợp với yêu cầu sau: "%s".
                Chỉ trả về tên và giá sản phẩm, không cần mô tả dài.
                """.formatted(context, prompt);

        String reply = geminiService.generateText(finalPrompt);
        return ResponseEntity.ok(reply);
    }

    // Lớp nhỏ để nhận dữ liệu JSON từ JS
    public static class PromptRequest {
        private String prompt;
        public String getPrompt() { return prompt; }
        public void setPrompt(String prompt) { this.prompt = prompt; }
    }
}
