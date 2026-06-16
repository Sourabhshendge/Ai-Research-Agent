package com.sourabh.rerank.onnx;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class OnnxModelLoader {

    private OrtEnvironment environment;
    private OrtSession session;

    @PostConstruct
    public void init() throws Exception {

        Path modelPath = Paths.get(
                "models/ms-marco-MiniLM-L-6-v2/onnx/model.onnx"
        );

        System.out.println("Current Dir: "
                + Paths.get("").toAbsolutePath());

        System.out.println("Model Path: "
                + modelPath.toAbsolutePath());

        System.out.println("Exists: "
                + Files.exists(modelPath));

        environment = OrtEnvironment.getEnvironment();

        session = environment.createSession(
                modelPath.toString(),
                new OrtSession.SessionOptions()
        );

        System.out.println(
                "Cross Encoder loaded successfully"
        );
    }

    public OrtEnvironment getEnvironment() {
        return environment;
    }

    public OrtSession getSession() {
        return session;
    }
}
