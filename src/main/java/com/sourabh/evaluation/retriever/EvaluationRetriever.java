package com.sourabh.evaluation.retriever;

import java.util.List;

public interface EvaluationRetriever {

    List<String> retrieve(
            String query,
            int topK
    );
}