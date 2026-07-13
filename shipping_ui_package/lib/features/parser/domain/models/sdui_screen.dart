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
    // Si el JSON viene envuelto en "screen", lo abrimos aquí mismo
    final Map<String, dynamic> data = json.containsKey('screen') 
        ? Map<String, dynamic>.from(json['screen']) 
        : json;

    return SDUIScreen(
      id: data['id'] ?? '',
      title: data['title'] ?? 'Sin Título',
      components: (data['components'] as List?)
              ?.map((e) => SDUIComponent.fromJson(Map<String, dynamic>.from(e)))
              .toList() ??
          [],
    );
  }
}
