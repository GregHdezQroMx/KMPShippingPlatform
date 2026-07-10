import 'sdui_component.dart';

class SDUIScreen {
  final String id;
  final String title;
  final List<SDUIComponent> components;

  SDUIScreen({
    required this.id,
    required this.title,
    required this.components,
  });

  factory SDUIScreen.fromJson(Map<String, dynamic> json) {
    final screen = json['screen'] ?? {};
    return SDUIScreen(
      id: screen['id'] ?? '',
      title: screen['title'] ?? '',
      components: (screen['components'] as List?)
              ?.map((e) => SDUIComponent.fromJson(e))
              .toList() ??
          [],
    );
  }
}
