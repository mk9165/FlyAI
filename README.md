## ‼️기획 의도
자동 통화 녹음, 꺼야할까? 켜야할까?
- 😊 통화 녹음의 장점: 보이스피싱, 협박, 괴롭힘 등 예상치 못한 상황에서 객관적 자료 확보
- 😒 통화 녹음의 단점: 일상대화, 스팸 등 불필요한 통화 내용까지 모두 저장

→ 쌓여가는 통화녹음, **필요한 통화녹음만** 저장할 수 있는 서비스 개발

---

## 📝프로젝트 Summary
### AI 기반 음성 데이터를 통한 '통화 녹음 저장 판단' 시스템

---

## 📞앱 이미지
![merge_from_ofoct](https://user-images.githubusercontent.com/74871378/221477298-cc9d97bc-b31a-4a6b-93f9-93aea51fe226.png)

---

## 개발 프레임워크
![image](https://user-images.githubusercontent.com/71757818/221478426-f6a518d6-5449-4ea2-82df-d56e737db08e.png)

---

## 🌐개발 환경
#### 💻Python
- 머신러닝 AI 모델 개발을 위한 프로그래밍 언어
 
#### 📱Android Studio
- SKT 연계 프로젝트로서, T전화 앱의 서비스 추가를 목표로 Android 개발 환경 채택
- 프로그래밍 언어 Java

#### 🖼️Figma
- Android UI와 Color Chip 구상

#### ☁️Azure Cloud
- 클라이언트와 통신하기 위한 서버
- 병렬화 가상머신 통한 모델 동작

---

## 감정 분석 모델
▶ 음성 특성 추출

▶ Spectrogram

▶ Wav2vec2.0

▶ BERT

---

## 요약 모델
▶ **첫 번째 시도**
- 길이가 긴 요약 데이터 제거
- 문서를 BART를 통해 요약

✔ 첫 번째 시도: 결과
- 애매한 성능
- STT 성능상의 단어 오류 존재
- 오타, 불용어 등 부적절한 단어들이 요약에 그대로 포함
- 문장 종료 토큰을 지정했음에도 문장이 마침표로 끝나지 않고 중간에 끊긴 문장이 등장

▶ **두 번째 시도**
- Embedding 벡터의 크기가 너무 작아 원문, 요약문의 특성을 모두 담지 못함 → 원문, 요약문의 텍스트 크기 분포를 각각 확인하여 Embedding 벡터 수정
- Kiwi 형태소 분석기를 사용 → 단어 오류, 맞춤법 교정, 불용어 제거 
- 마침표로 끝나지 않는 문장 제거

✔ 두 번째 시도: 결과
- 불필요한 단어들이 제거된 탓에 요약 문장이 매끄럽지 않음 → Kiwi 형태소 분석기 사용 기각
- 마침표로 끝나는 온전한 문장들만 등장
- 길이가 너무 짧아 의미가 없는 문장이 등장 + 문장 수가 많은 요약문 등장

▶ **세 번째 시도**
- 너무 짧은 문장 제거, 문장 개수 제한

✔ 세 번째 시도: 결과
- 한 두 문장으로 구성된 요약문 생성 성공

---

## 키워드 모델
▶ **첫 번째 시도**
- 문서에서 Sklearn의 CountVectorizer를 사용하여 키워드 추출
- 문서와 문서로부터 추출한 키워드를 BERT를 통해 Embedding
- Cosine Similarity를 사용해 문서와 가장 유사한 키워드 추출

✔ 첫 번째 시도: 결과
- STT 성능상의 단어 오류 존재
- 동사형 등 키워드에 부적합한 단어 등장
- 비슷한 의미의 키워드들이 리턴

▶ **두 번째 시도**
- Kiwi 형태소 분석기를 사용 → 단어 오류, 맞춤법 교정, 불용어 제거, 명사만 추출

✔ 두 번째 시도: 결과
- 자연스러운 키워드 추출 성공
- 여전히 비슷한 의미의 키워드들이 리턴

▶ **세 번째 시도**
- MMR(Maximal Marginal Relevance) 사용 → 중복을 최소화하고 결과의 다양성 극대화<br>
       1) 문서와 가장 유사한 키워드 선택<br>
       2) 문서와 유사하고 이미 선택된 키워드와 유사하지 않은 새로운 키워드를 반복적으로 선택 

✔ 세 번째 시도: 결과
- 주제 관련성이 높은 키워드 추출
- 키워드 중복을 방지하여 다양한 키워드 추출

---

## 🧑‍💻역할
**성시열**()

**김지환**([https://github.com/hongju904](https://github.com/hongju904))
- Android 앱 UI/UX 개발 ([구현 내용](https://velog.io/@hongju904/VoiceKeeper-app-%EA%B0%9C%EB%B0%9C-%EA%B8%B0%EB%A1%9D))
- Cloud 서버와 Android 클라이언트 HTTP 통신

**전민기**(https://github.com/mk9165)
- 텍스트 감성 분류 모델
- 대화 요약, 키워드 추출

**최형준**()

---

## 🧐문제점 및 해결 내용



