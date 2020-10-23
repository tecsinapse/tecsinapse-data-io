/*
 * Tecsinapse Data Input and Output
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later
 * See the LICENSE file in the root directory or <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package br.com.tecsinapse.dataio.util;


import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReflectionUtil {

    public static <T> void setConstructorAccessible(final Constructor<T> constructor) {
        if (constructor == null || constructor.isAccessible()) {
            return;
        }
        constructor.setAccessible(true);
    }

    public static <T> void setMethodAccessible(final Method method) {
        if (method == null || method.isAccessible()) {
            return;
        }
        method.setAccessible(true);
    }

    public static <T> T newInstance(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
    }

}
