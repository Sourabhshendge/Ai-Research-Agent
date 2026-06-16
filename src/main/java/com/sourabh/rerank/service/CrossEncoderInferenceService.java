package com.sourabh.rerank.service;

import ai.onnxruntime.*;
import com.sourabh.rerank.dto.CrossEncoderInput;
import com.sourabh.rerank.onnx.OnnxModelLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CrossEncoderInferenceService {

    private final OnnxModelLoader modelLoader;

    public float score(CrossEncoderInput input)
            throws OrtException {

        OrtEnvironment env =
                modelLoader.getEnvironment();

        OrtSession session =
                modelLoader.getSession();

        long[][] inputIds =
                new long[][]{input.inputIds()};

        long[][] attentionMask =
                new long[][]{input.attentionMask()};

        long[][] tokenTypeIds =
                new long[][]{
                        input.tokenTypeIds()
                };

        try (
                OnnxTensor tokenTypeTensor =
                        OnnxTensor.createTensor(
                                env,
                                tokenTypeIds
                        );
                OnnxTensor inputIdsTensor =
                        OnnxTensor.createTensor(
                                env,
                                inputIds
                        );

                OnnxTensor attentionMaskTensor =
                        OnnxTensor.createTensor(
                                env,
                                attentionMask
                        )

        ) {

            Map<String, OnnxTensor> inputs =
                    Map.of(
                            "input_ids",
                            inputIdsTensor,
                            "attention_mask",
                            attentionMaskTensor,
                            "token_type_ids",
                            tokenTypeTensor
                    );

            OrtSession.Result result =
                    session.run(inputs);

            Object output = result.get(0).getValue();

            System.out.println("Output Type = " + output.getClass());

            if (output instanceof float[][] logits) {

                System.out.println(
                        "Logits Length = " + logits.length
                );

                System.out.println(
                        "Score = " + logits[0][0]
                );

                return logits[0][0];
            }

            throw new RuntimeException(
                    "Unexpected output type: "
                            + output.getClass()
            );
        }
    }
}