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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class TypedAdapter extends RecyclerView.Adapter<TypeViewHolder<Object>> {

    private final TypeRegistry mTypeRegistry;

    private final ItemsDiffCallback mItemsDiffCallback;

    @NonNull
    private List<Object> mItems = Collections.emptyList();


    public TypedAdapter(@NonNull TypeRegistry typeRegistry) {
        mTypeRegistry = typeRegistry;
        mItemsDiffCallback = new ItemsDiffCallback(typeRegistry);

        setHasStableIds(true);
    }

    public void submitList(@Nullable List<Object> list) {
        list = list == null ? Collections.emptyList() : list;
        final DiffUtil.DiffResult result =
                DiffUtil.calculateDiff(new TypedDiffCallback(mItems, list, mItemsDiffCallback));
        mItems = Collections.unmodifiableList(list);
        result.dispatchUpdatesTo(this);
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

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    protected Object getItem(int position) {
        return mItems.get(position);
    }

    @NonNull
    public List<Object> getCurrentList() {
        return mItems;
    }


    private static final class TypedDiffCallback extends DiffUtil.Callback {
        private final List<Object> mOldItems;
        private final List<Object> mNewItems;
        private final ItemsDiffCallback mItemsDiffCallback;

        public TypedDiffCallback(
                @NonNull List<Object> oldItems,
                @NonNull List<Object> newItems,
                @NonNull ItemsDiffCallback itemsDiffCallback
        ) {
            mOldItems = oldItems;
            mNewItems = newItems;
            mItemsDiffCallback = itemsDiffCallback;
        }

        @Override
        public int getOldListSize() {
            return mOldItems.size();
        }

        @Override
        public int getNewListSize() {
            return mNewItems.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mItemsDiffCallback.areItemsTheSame(
                    mOldItems.get(oldItemPosition),
                    mNewItems.get(newItemPosition)
            );
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return mItemsDiffCallback.areContentsTheSame(
                    mOldItems.get(oldItemPosition),
                    mNewItems.get(newItemPosition)
            );
        }
    }

}
