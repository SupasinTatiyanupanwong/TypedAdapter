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
import androidx.recyclerview.widget.DiffUtil;

class ItemsDiffCallback extends DiffUtil.ItemCallback<Object> {

    private final TypeRegistry mRegistry;

    ItemsDiffCallback(@NonNull TypeRegistry registry) {
        mRegistry = registry;
    }


    @Override
    public boolean areItemsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
        return mRegistry.getItemId(oldItem) == mRegistry.getItemId(newItem);
    }

    @Override
    public boolean areContentsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
        if (oldItem.getClass() == newItem.getClass()) {
            return mRegistry.getTypeProvider(oldItem).areContentsTheSame(oldItem, newItem);
        } else {
            return false;
        }
    }

}
