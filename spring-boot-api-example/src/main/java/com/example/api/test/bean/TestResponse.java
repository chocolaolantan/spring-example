package com.example.api.test.bean;

import com.example.common.response.ResponseBean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Test用、レスポンスBeanクラス.
 * @author chocolaolantan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class TestResponse extends ResponseBean
{
	/** テキスト文字列. */
	private String text;
}
