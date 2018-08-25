package com.writersnets.models.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class EstimateRequest {
    @NotNull
    private Long bookId;

    @NotNull
    @Min(1)
    @Max(5)
    private Short estimation;
}
