/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.core;

import java.util.function.Function;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Interface defining a generic contract for attaching and accessing metadata
 * to/from arbitrary objects.
 *
 * @author Rob Harrop
 * @author Sam Brannen
 * @since 2.0
 */
// 用于获取元数据，在实现类（eg:AttributeAccessorSupport）中通过LinkHashMap集合保存元素据。
// 例如：通过XML的<meta>标签定义的一些元数据会保存其中
public interface AttributeAccessor {

	/**
	 * Set the attribute defined by {@code name} to the supplied {@code value}.
	 * <p>If {@code value} is {@code null}, the attribute is {@link #removeAttribute removed}.
	 * <p>In general, users should take care to prevent overlaps with other
	 * metadata attributes by using fully-qualified names, perhaps using
	 * class or package names as prefix.
	 * @param name the unique attribute key
	 * @param value the attribute value to be attached
	 */
	// 设置属性值
	void setAttribute(String name, @Nullable Object value);

	/**
	 * Get the value of the attribute identified by {@code name}.
	 * <p>Return {@code null} if the attribute doesn't exist.
	 * @param name the unique attribute key
	 * @return the current value of the attribute, if any
	 */
	@Nullable
	// 获取属性值
	Object getAttribute(String name);

	/**
	 * Compute a new value for the attribute identified by {@code name} if
	 * necessary and {@linkplain #setAttribute set} the new value in this
	 * {@code AttributeAccessor}.
	 * <p>If a value for the attribute identified by {@code name} already exists
	 * in this {@code AttributeAccessor}, the existing value will be returned
	 * without applying the supplied compute function.
	 * <p>The default implementation of this method is not thread safe but can
	 * overridden by concrete implementations of this interface.
	 * @param <T> the type of the attribute value
	 * @param name the unique attribute key
	 * @param computeFunction a function that computes a new value for the attribute
	 * name; the function must not return a {@code null} value
	 * @return the existing value or newly computed value for the named attribute
	 * @see #getAttribute(String)
	 * @see #setAttribute(String, Object)
	 * @since 5.3.3
	 */
	//如有必要，为由name标识的属性计算一个新值，并在此AttributeAccessor 设置新值。
	//如果此AttributeAccessor已经存在通过name标识的AttributeAccessor的值，则将不使用提供的计算功能而返回现有值。
	//此方法的默认实现不是线程安全的，但可以被此接口的具体实现覆盖。
	@SuppressWarnings("unchecked")
	default <T> T computeAttribute(String name, Function<String, T> computeFunction) {
		Assert.notNull(name, "Name must not be null");
		Assert.notNull(computeFunction, "Compute function must not be null");
		Object value = getAttribute(name);
		if (value == null) {
			value = computeFunction.apply(name);
			Assert.state(value != null,
					() -> String.format("Compute function must not return null for attribute named '%s'", name));
			setAttribute(name, value);
		}
		return (T) value;
	}

	/**
	 * Remove the attribute identified by {@code name} and return its value.
	 * <p>Return {@code null} if no attribute under {@code name} is found.
	 * @param name the unique attribute key
	 * @return the last value of the attribute, if any
	 */
	// 删除并返回属性值
	@Nullable
	Object removeAttribute(String name);

	/**
	 * Return {@code true} if the attribute identified by {@code name} exists.
	 * <p>Otherwise return {@code false}.
	 * @param name the unique attribute key
	 */
	// 判断属性是否存在
	boolean hasAttribute(String name);

	/**
	 * Return the names of all attributes.
	 */
	//获取所有属性名
	String[] attributeNames();

}
