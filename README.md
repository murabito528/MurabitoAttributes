# MurabitoAttributes

Path of Exile (PoE) のようなステータスシステムをMinecraftに導入するModです。

[![CurseForge](https://img.shields.io/badge/CurseForge-View_Project-orange?style=for-the-badge&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/murabitoattributes)

##  概要
属性変換、貫通、Affixシステムなど、PoEライクなシステムを追加します。

Modding初心者による制作のため、コードに粗が目立つ部分があるかもしれません。
追加されるカスタムAttributesは、以下のファイルをご覧ください。

[`CustomAttributes.java`](src/main/java/com/murabito/murabitoattributesmod/attributes/CustomAttributes.java)

---

## 進捗状況

### 実装済み
* **基本的なPoEライク属性の追加**
* **PoEライクなダメージ計算処理**
    * 属性変換 / 属性貫通 / 属性耐性 等を含む処理
* **Affix管理コマンド**
    * コマンドを使用したアイテムへの **iLvl** および **Affix** の追加
* **データパックによるAffix管理**
    * 利用方法はこちらを参照:[`src/main/resources/data/murabitoattributes/affix`](src/main/resources/data/murabitoattributes/affix)

### 作業中
* **状態異常**: 凍結・感電まで完成 Dot系は未完成
* **クラフトシステム**: カレンシーを使用したAffix付与
* **特殊ステータスの調整**: 主にLeech関連の挙動

### 実装予定
* 一部のMobに対するデフォルトの属性変換や耐性などの配布
* より複雑なPoEライク属性（エナジーシールド、ワードなど）
* レベルに応じたアイテムドロップシステム
    * ※他レベル追加Mod等との連携を検討中

---

## 動作環境
* **対応バージョン:** `1.20.1`
* **Modローダー:** `Forge`

## 連携要素のあるMod
* **Iron's Spells 'n Spellbooks**:元素ダメージ増加や耐性などの影響を受けるようになります
* **Target Dummy**:元素/混沌ダメージは色付きで表示

## 推奨Mod
* **Apothic Attributes**:物理ダメージ計算式を変更します
* **AttributeFix**:防御力の上限(バニラでは30)を無くします
* **Max Health Fix**:ワールドに入りなおしたとき体力が20以上であっても20になってしまうバグを修正します

[CurseForge Project Page](https://www.curseforge.com/minecraft/mc-mods/murabitoattributes)