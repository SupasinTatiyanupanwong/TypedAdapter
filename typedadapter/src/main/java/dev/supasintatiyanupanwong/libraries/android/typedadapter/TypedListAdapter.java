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

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

public class TypedListAdapter extends ListAdapter<Object, TypeViewHolder<Object>> {

    private final TypeRegistry mTypeRegistry;

    public TypedListAdapter(@NonNull TypeRegistry typeRegistry) {
        super(new ItemsDiffCallback(typeRegistry));

        mTypeRegistry = typeRegistry;

        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return mTypeRegistry.getItemId(getItem(position));
    }

    @Override
    public int getItemViewType(int position) {
        return mTypeRegistry.getItemViewType(getItem(position));
    }

    @NonNull
    @Override
    public TypeViewHolder<Object> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return mTypeRegistry.getTypeProvider(viewType).onCreateViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder<Object> holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public void onViewRecycled(@NonNull TypeViewHolder<Object> holder) {
        holder.unbind();
    }

}
