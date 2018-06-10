package com.example.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 本APIのレスポンスBeanはこれを継承すること.
 * @author chocolaolantan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ResponseBean
{
	/** 処理結果. */
	private String status;
}
