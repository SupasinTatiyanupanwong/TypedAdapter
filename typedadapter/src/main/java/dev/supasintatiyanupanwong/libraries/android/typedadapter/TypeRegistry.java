/*
 * Copyright 2021 Supasin Tatiyanupanwong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.supasintatiyanupanwong.libraries.android.typedadapter;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class TypeRegistry {

    private final Map<Class<?>, Integer> mViewTypes = new HashMap<>();
    private final Map<Integer, TypeProvider<Object>> mItemProviders = new HashMap<>();
    private final Map<Integer, Map<Object, Long>> mStableIds = new HashMap<>();

    private long mLastGeneratedId = 0L;

    public void registerTypeProvider(@NonNull TypeProvider<?> provider) {
        final int viewType = mViewTypes.size();
        mViewTypes.put(provider.getItemViewType(), viewType);
        //noinspection unchecked
        mItemProviders.put(viewType, (TypeProvider<Object>) provider);
    }

    public TypeProvider<Object> getTypeProvider(@NonNull Object item) {
        return getTypeProvider(getItemViewType(item));
    }

    TypeProvider<Object> getTypeProvider(int viewType) {
        final TypeProvider<Object> provider = mItemProviders.get(viewType);
        if (provider != null) {
            return provider;
        } else {
            throw new IllegalArgumentException("Can't find provider for type " + viewType);
        }
    }

    int getItemViewType(@NonNull Object item) {
        final Integer viewType = mViewTypes.get(item.getClass());
        if (viewType != null) {
            return viewType;
        } else {
            throw new IllegalArgumentException("Can't find view type for item " + item);
        }
    }

    long getItemId(@NonNull Object item) {
        final int viewType = getItemViewType(item);

        // Getting ids map, unique for each item type
        final Map<Object, Long> typeIds = $Map$getOrPut(mStableIds, viewType, new HashMap<>());
        // Getting original item id
        final Object origId = getTypeProvider(viewType).getItemId(item);
        // Generating stable ids for all newly detected ids
        return $Map$getOrPut(typeIds, origId, mLastGeneratedId++);
    }


    private static <K, V> V $Map$getOrPut(@NonNull Map<K, V> map, @NonNull K key, V defaultValue) {
        final V value = map.get(key);
        if (value == null) {
            map.put(key, defaultValue);
            return defaultValue;
        } else {
            return value;
        }
    }

}
