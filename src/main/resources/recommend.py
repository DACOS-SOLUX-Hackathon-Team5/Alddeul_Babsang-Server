import pandas as pd
import sys
import random
import ast
import json

#스프링에 json으로 표준 입력 받기
sys.stdout.reconfigure(encoding='utf-8')
input_bytes = sys.stdin.buffer.read()
input_json = input_bytes.decode("utf-8")

print("repr(input_json):", repr(input_json), flush=True)
if input_json.startswith("'") and input_json.endswith("'"):
    input_json = input_json[1:-1]
#받은 거 출력
print(input_json)

#JSON 데이터 프레임으로 변환
try:
    store_list = json.loads(input_json)
    print("파싱 성공!")
except json.JSONDecodeError as e:
    print("JSON 파싱 실패!")
    print("에러 메시지:", str(e))
df = pd.DataFrame(store_list)

#기존 entity컬럼 명을 파이썬에서 사용하기 위한 리네이밍
df = df.rename(columns={
    "region": "구",
    "category": "분류코드명",
    "top5Tags": "Top 5 Tags and Counts",
    "name": "업소명"
})
print(df.to_string(), flush=True)




#개인 구 추출
def get_gu(liked_stores):
  count = liked_stores['구'].value_counts().head(4)
  return count.index.tolist()

#개인 카테고리  추출
def get_category(liked_stores):
  count = liked_stores['분류코드명'].value_counts().head(4)
  return count.index.tolist()

#개인 태그 추출
def get_tag(liked_stores):
  all_tag = []
  for tags in liked_stores['Top 5 Tags and Counts']:
    tags = tags.replace('[', '')
    tags = tags.replace(']', '')
    tags = tags.split(',')
    tags = [tag.strip() for tag in tags]
    for tag in tags:
      all_tag.append(tag)
  count = pd.Series(all_tag).value_counts().head(4)
  return count.index.tolist()

def get_similar_stores(df_recommended, liked_categories, liked_tags, liked_gus):
     similar_stores = []
     print("similar 함수 실행중")

     for idx, store in df_recommended.iterrows():
         # 'Top 5 Tags and Counts'에서 태그를 가져오고, 괄호 및 공백 제거
         tags = store['Top 5 Tags and Counts']
         tags = tags.replace('[', '').replace(']', '')  # 괄호 제거
         tags = tags.split(',')  # 쉼표 기준으로 분리
         tags = [tag.strip() for tag in tags]  # 앞뒤 공백 제거

         # 카테고리: 특정된 카테고리 중 하나라도 일치하는지 확인
         if store["분류코드명"] in liked_categories:
             # 구: 특정된 구 중 하나라도 일치하는지 확인
             if store["구"] in liked_gus:
                 # 태그: 특정된 태그가 모두 포함되어 있는지 확인
                 if all(tag in liked_tags for tag in tags):
                     similar_stores.append(store["업소명"])

     if len(similar_stores) >= 4:
       random_stores = random.sample(similar_stores, 4)  # 랜덤으로 4개 선택
       return random_stores
     else: # 4개 안되면 나머지는 랜덤 출력, 좋아요 누른 가게가 적은 경우 -> 완전히 새로운 랜덤한 가게 출력
       remaining_stores = df_recommended['업소명'].tolist()
       remaining_stores = [store for store in remaining_stores if store not in similar_stores]  # 이미 추천된 가게는 제외
       random_stores = random.sample(remaining_stores, 4 - len(similar_stores))  # 부족한 만큼 랜덤으로 추가
       similar_stores.extend(random_stores)
       return similar_stores

liked_categories = get_category(df)
liked_tags = get_tag(df)
liked_gus = get_gu(df)
try:
    df_recommended = pd.read_csv("src/main/resources/df_recommended.csv", encoding='cp949')
except Exception as e:
    print("에러 메시지:", str(e), flush=True)


#
#  유사한 가게들 추천
recommended = get_similar_stores(df_recommended, liked_categories, liked_tags, liked_gus)
# #출력 통해 스프링으로 전달
print("recommended: ",recommended, flush=True)
