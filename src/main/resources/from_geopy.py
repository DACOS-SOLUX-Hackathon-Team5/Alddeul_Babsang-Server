from geopy.distance import geodesic
import pandas as pd
import sys
import json


sys.stdout.reconfigure(encoding='utf-8')

#json받아옴
input_bytes = sys.stdin.buffer.read()
input_json = input_bytes.decode("utf-8")

print("repr(input_json):", repr(input_json), flush=True)
if input_json.startswith("'") and input_json.endswith("'"):
    input_json = input_json[1:-1]
#받은 거 출력
print(input_json)

#JSON 데이터 프레임으로 변환
try:
    store_info = json.loads(input_json)
    print("파싱 성공!")
except json.JSONDecodeError as e:
    print("JSON 파싱 실패!")
    print("에러 메시지:", str(e))

df = pd.DataFrame([store_info])

menu = df.rename(columns={
    "storeRealId": "업소아이디",
    "latitude": "latitude",
    "longitude": "longitude",
    "clusterId": "cluster2"
})
try:
    df_re = pd.read_csv("src/main/resources/df_recommended (1).csv", encoding='utf-8')
    print("에러없음")
except Exception as e:
    print("에러 메시지:", str(e), flush=True)


print(menu)
# 같은 cluster2에 있는 가장 가까운 4개의 가게를 찾는 함수
def find_nearest_stores(df_recommended, menu, num_stores=4):
    print("함수작동중")

    # 입력 받은 가게 데이터에서 위도, 경도, 클러스터 값 가져오기
    input_lat = menu['latitude'].values[0]
    print(input_lat)

    input_long = menu['longitude'].values[0]
    print(input_long)
    target_cluster = menu['cluster2'].values[0]
    print(target_cluster)
    # 동일한 클러스터에 있는 가게들 필터링 (본인 제외)
    same_cluster_data = df_recommended[(df_recommended['cluster2'] == target_cluster) &
                                       (df_recommended['업소아이디'] != menu['업소아이디'].values[0])].copy()

    # 거리 계산 후 정렬
    print("🟢 거리 계산 시작", flush=True)
    same_cluster_data['distance'] = same_cluster_data.apply(
        lambda row: geodesic((input_lat, input_long), (row['latitude'], row['longitude'])).meters, axis=1
    )
    print("🟢 거리 계산 완료", flush=True)
    nearest_stores = same_cluster_data.sort_values(by='distance').head(num_stores)
    return nearest_stores[['업소아이디']]

result_df = find_nearest_stores(df_re, menu)
try:
    print(json.dumps(result_df["업소아이디"].astype(int).tolist(), ensure_ascii=False), flush=True)
except Exception as e:
    print("🚨 에러 발생:", str(e), flush=True)
