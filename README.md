# JPA 공부

### ORM(Object Relational Mapping)

자동으로 쿼리가 작성됨.
객체 진영과 RDB 진영을 자동으로 매핑하여 구조의 불일치를 개발자 대신 해결해주는 기술의 총칭이다.
객체 지향 구조에서 프로그래밍 언어를 사용하여 RDB의 데이터를 조작하는 방법이다.
ORM을 사용하면 개발자가 SQL문을 직접 작성하지 않아도 RDB와 상호작용 할 수 있다.

### JPA(Java Persistence API)

ORM을 Java에 특화시킴	
ORM을 사용하기 위한 설계도(틀)이다.
Java Application용 RDB 매핑 관리를 위한 인터페이스 이며, DBMS 벤더사에 의존하지 않고 독립적으로 ORM을 사용할 수 있는 ORM 표준이다.	
인터페이스이기 때문에 구현되어 있지 않은 틀만 제공하며, 자체적인 작업을 수행하지 않는다.
JPA에 설계된 구조에 맞춰서 각 메소드를 재정의하여 직접 ORM을 구현하여 사용해야 한다.
JPA는 ORM을 사용할 수 있는 JAVA 인터페이스를 제공하는 ORM접근 방식이며, 구현되지 않은 JPA를 ORM이라고 말하기는 어렵다.

### Hibernate Framework

모든 Java  Application에 대해 객체 관계를 그대로 유지한 채 쿼리 서비스를 제공하는 오픈 소스의 경량 ORM이다.
JPA를 구현한 구현체이며, 여러 구현체 중 가장 대표적인 구현체이다.
객체 간 관계 구성을 지원하며, 상속, 지연성, 페이징 처리, 예외처리 불필요를 지원한다.
예외처리 불필요란, JPA만의 독자적인 예외를 생성하여 try catch 및 throws를 강제시키지 않고
트랜잭션을 지원하는 Spring Framework가 추상화한 예외로 변환 시켜 커밋하지 않고 롤백시키도록 해준다.

Application ---------> JPA 표준 인터페이스
                       	▲
                        │
                        │
		   ┌────────┼──────────┐
                         │        |          │	
	    Hibernate  EclipseLink DataNucleus

### Spring Data JPA

JPA를 추상화한 Repository 인터페이스를 제공하여 JPA를 쓰기 편하게 다양한 기능을 지원한다.
내부적으로는 JPA를 사용한다. 그래서 JPA를 모르면 내부 구조를 이해하기 힘들 수 있다.

### 객체와 관계형 데이터 베이스의 차이

1. 상속

[개발자]		  [기획자]
번호		           번호
---------------	    -------
이름		           이름
생년월일		   생년월일
경력		           경력
기술등급		    OA등급
프로젝트 수        클라이언트 수

●●●●●●●●●●●●●●또는●●●●●●●●●●●●●●

▷ 슈퍼 - 서브 타입 도출

[사원] - 슈퍼
번호(PK)
--------
이름
생년월일
경력
기술등급
프로젝트 수
OA등급
클라이언트 수

[개발자]-서브	     [기획자]- 서브

번호(PK, FK)	     번호(PK, FK)
---------------	      -------
경력		              경력
기술등급		       OA등급
프로젝트 수	      클라이언트 수

1:1 관계에서 INSERT를 하기 위해서는 쿼리를 2번 작성해야하는 불편함이 생긴다.
게다가 조회를 할 때에는 JOIN을 사용해야 하는데 쿼리가 굉장히 복잡해진다.
만약에 이런 RDB의 테이블 관계를 자바 컬렉션으로 바꿀 수 있다면,

Developer developer = list.get(developerId);

1. 연관관계

객체는 하위 연산자(.)를 사용하여 참조를 한다.

▶객체 연관관계 : 단방향으로 흘러간다(Flower에서 Pot에 접근은 가능하지만 반대는 불가능)

Flower 		→ 		Pot

id					id
flowerName				shape
Pot pot				color

▶RDB 연관 관계 : 양방향으로 흘러간다(Flower에서 Pot을, Pot에서 Flower를 접근할 수 있다.

Flower 		↔		Pot

FLOWER_ID(PK)			POT_ID(PK)
FLOWER_NAME				POT_SHAPE
POT_ID(FK)				POT_COLOR

▶ 객체를 테이블에 맞추어 설계

Class Flower{
	String flowerId;
	String flowerName;
	String potId; // FK는 RDB방식에서의 연관관계이기 때문에 객체방식으로 바꿔야 함.
}

▶ 테이블을 객체에 맞추어 설계

Class Flower{
	String flowerId;
	String flowerName;
	Pot pot; // 참조로 연관관계를 맺도록 함.
}

위와 같이 RDB를 객체방식으로 설계하면,
조회 시 JOIN을 하여 FLOWER와 POT에서 각각 필요한 정보를 가져와
각 객체에 담아준 뒤, flower.setPot(pot) 형태와 같이 복잡하게 작업해야 한다.

하지만 만약 자바 컬렉션으로 관리가 가능하다면,

list.add(꽃);
Flower flower = list.get(flowerId);
Pot pot = flower.getPot();

훨씬 편하게 작업이 가능하다.

1. 그래프 탐색

Market
│
│
Flower──Pot
│
│
Order──Client
│
│
Delivery

객체는 모든 객체 그래프를 탐색할 수 있어야 한다.

market.getFlower();
flower.getPot();
...

하지만 SQL 작성 시 이미 탐색 범위가 결정된다.
만약 Market과 Flower를 JOIN해서 조회를 한다면,

market.getFlower()는 사용 가능하지만
market.getPot()는 null일 수 밖에 없다.

따라서 엔티티에 대한 신뢰가 무너질 수 밖에 없다.

Market market = marketDAO.find(marketId);
market.getFlower(); // null이 아니라고 확신할 수 없다.
market.getOrder().getClient(); // null이 아니라고 확실할 수 없다.

marketDAO에 있는 find()를 분석해보지 않는 이상 각 엔티티에 대해 신뢰할 수 없다.

따라서 상황에 따라 조회에 대한 메소드를 여러 개 선언해놓아야 한다.

marketDAO.getFlower();
marketDAO.getMemberWithFlower();
marketDAO.getMemberWithOrderWithClient();
...

하지만 위와 같은 방법은 사실상 불가능에 가깝다.

1. 값 비교

SQL 실행 결과를 담은 뒤 생성자를 호출하여 객체에 담으면 매번 new가 사용되기 때문에
동일한 조회 결과의 객체일지라도 주소가 모두 다르다.

하지만 만약 자바 컬렉션에서 객체 조회가 가능하다면
list.get(memberId) == list.get(memberId)
같은 객체를 가져오기 때문에 주소가 같다.

즉, 객체지향으로 설계할 수록 작업이 오히려 복잡해지고 늘어나기 때문에 RDB 중심으로 설계할 수 밖에 없다.
RDB를 자바 컬렉션에 저장하듯 사용하면 굉장히 편해지고 많은 문제가 해결되는데,
바로 이 기술을 JPA(Java Persistence API)라고 한다.

## JPA를 사용해야 하는 이유

1. SQL 중심 개발에서 객체 중심으로 개발
2. 생산성

저장 : jpa.persist(market);
조회 : jpa.find(marketId);
수정 : market.setMarketName("새로운 이름");
삭제 : jpa.remove(market);

1. 유지보수

클라이언트가 새로운 필드를 요청하여 새로운 필드 추가 시
클래스 안에 필드만 한 개 추가하면 끝. SQL문을 수정할 필요 없음.

1. 패러다임의 불일치 해결

▶ JPA와 상속

Employee      Developer extends Employee

employeeId      developerId
employeeName      developerLevel
projectCount

-INSERT

▷ 개발자
jpa.persist(developer);

▷ JPA
JPA가 알아서 INSERT 두 번 해줌.

자식 필드에 부모 필드가 포함되어 있기 때문에 필요한 데이터를 자식 객체에 채우기만 하면 됨.

-SELECT
▷ 개발자
jpa.find(Developer.class, developerId);

▷ JPA
JPA가 알아서 부모 테이블(Employee)과 JOIN해서 데이터를 가져옴.

▶ JPA와 연관관계

Flower   →   Pot

flowerId   	potId
name      	shape
pot      	color

flower.setPot(pot);
jpa.persist(꽃);

jpa.find(Flower.class, flowerId);

▶ JPA와 객체 그래프 탐색

Market
│
│
Flower──Pot
│
│
Order──Client
│
│
Delivery

Flower flower = jpa.find(Flower.class, flowerId);
Pot pot = flower.getPot();

marketDAO.find(marketId);
market.getFlower();
market.getOrder().getClient();

※ SELECT 결과가 없으면 문제가 생기기 때문에 NPE 체크는 반드시 해야 한다.

▶ JPA와 값 비교
Market market1 = jpa.find(Market.class, marketId);
Market market2 = jpa.find(Market.class, marketId);

market1 == market2;

동일한 트랜잭션에서 조회한 엔티티는 무조건 같다.
