[English](README.md) | ����

+ **Discord ��������**
  [![Discord](https://img.shields.io/discord/825801593466388520.svg?logo=discord&logoColor=white&logoWidth=20&labelColor=7289DA&label=Discord&color=17cf48)](https://discord.gg/yNmbMcuwyW)
+ **QQ**��676206144
  ![](images/qq-QRcode.png)

## ��Github Action����

����ֿ��ѿ���Github Action CI���Զ�����
1. ���Actions���������ĳɹ���������ʾ��ɫ���ţ���
2. ������󣬵��Disintegration������**ѹ����jar�ļ�**���޷�ֱ�ӷ�����Ϸ����ѹ�ļ���ſ��Է�����Ϸmod�ļ��С�
* ���˷������Թ�����׿�͵��Զ����õ��ļ���

## ���ع���

* ### �����԰淽����
1. ��װ JDK **17**��
2. ���� `gradlew jar`��
3. mod�ļ�������`build/libs` ·����

* ### ��׿�͵��԰淽����
  �����Ƽ���ֻ��action builds�����õ�ʱ�����ã�
1. ����Android SDK, ��ѹ���� `ANDROID_HOME` ������������Ϊ���İ�װ·����*�ٷ�discord��������`modding-general`Ƶ�������Զ����õ��������ļ���*
2. ȷ���㰲װ��API level 30�� ���������İ汾�� (���� 30.0.1)
3. ��build-tools����·���� ����, �����װ�İ汾�� `30.0.1`�� �Ǿ��� `$ANDROID_HOME/build-tools/30.0.1`��
4. ���� `gradlew deploy`�� �����������, mod�ļ������� `build/libs` ·����