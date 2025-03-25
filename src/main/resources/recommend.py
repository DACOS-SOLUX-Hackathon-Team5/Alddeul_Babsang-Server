import pandas as pd
import sys
import random


sys.stdout.reconfigure(encoding="utf-8")


def get_gu(liked_stores):
    count = liked_stores["region"].value_counts().head(4)
    return count.index.tolist()


def get_category(liked_stores):
    count = liked_stores["category"].value_counts().head(4)
    return count.index.tolist()


def get_tag(liked_stores):
    all_tag = []
    for tags in liked_stores["tag"]:
        tags = tags.replace("[", "").replace("]", "")
        tags = tags.split(",")
        tags = [tag.strip() for tag in tags]
        all_tag.extend(tags)
    count = pd.Series(all_tag).value_counts().head(4)
    return count.index.tolist()


def get_similar_stores(df_recommended, liked_categories, liked_tags, liked_gus):
    similar_stores = []

    for idx, store in df_recommended.iterrows():
        # 'Top 5 Tags and Counts'에서 태그를 가져오고, 괄호 및 공백 제거
        tags = store["tag"]
        tags = tags.replace("[", "").replace("]", "")  # 괄호 제거
        tags = tags.split(",")  # 쉼표 기준으로 분리
        tags = [tag.strip() for tag in tags]  # 앞뒤 공백 제거

        # 카테고리: 추천된 카테고리 중 하나라도 일치하는지 확인
        if store["category"] in liked_categories:
            # 구: 추천된 구 중 하나라도 일치하는지 확인
            if store["region"] in liked_gus:
                # 태그: 추천된 태그가 모두 포함되어 있는지 확인
                if all(tag in liked_tags for tag in tags):
                    similar_stores.append(store["name"])

    if len(similar_stores) >= 4:
        random_stores = random.sample(similar_stores, 4)  # 랜덤으로 4개 선택
        return random_stores
    else:  # 4개 안되면 나머지는 랜덤 출력, 좋아요 누른 가게가 적은 경우 -> 완전히 새로운 랜덤한 가게 출력
        remaining_stores = df_recommended["name"].tolist()
        remaining_stores = [
            store for store in remaining_stores if store not in similar_stores
        ]  # 이미 추천된 가게는 제외
        random_stores = random.sample(
            remaining_stores, 4 - len(similar_stores)
        )  # 부족한 만큼 랜덤으로 추가
        similar_stores.extend(random_stores)
        return similar_stores


df_recommended = pd.read_csv(
    r"src/main/resources/df_recommended (1).csv"
)

similar_stores = get_similar_stores(
    df_recommended, sys.argv[1], sys.argv[2], sys.argv[3]
)
# Java에서 읽을 수 있도록 출력
for store in similar_stores:
    print(store)
