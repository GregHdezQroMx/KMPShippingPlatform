enum ComponentType {
  textInput,
  select,
  button,
  text,
  image,
  card,
  icon,
  unknown;

  static ComponentType fromString(String type) {
    return ComponentType.values.firstWhere(
      (e) => e.name == _toCamelCase(type),
      orElse: () => ComponentType.unknown,
    );
  }

  static String _toCamelCase(String text) {
    List<String> parts = text.split('_');
    if (parts.length == 1) return text;
    return parts[0] + parts.skip(1).map((e) => e[0].toUpperCase() + e.substring(1)).join();
  }
}

class SDUIComponent {
  final String id;
  final ComponentType type;
  final String label;
  final String? inputType;
  final String? defaultValue;
  final List<Map<String, String>>? options;
  final Map<String, dynamic>? action;
  final List<Map<String, dynamic>>? validations;
  final String? style;
  final String? imageUrl;
  final String? iconName;
  final List<SDUIComponent>? children;

  SDUIComponent({
    required this.id,
    required this.type,
    required this.label,
    this.inputType,
    this.defaultValue,
    this.options,
    this.action,
    this.validations,
    this.style,
    this.imageUrl,
    this.iconName,
    this.children,
  });

  factory SDUIComponent.fromJson(Map<String, dynamic> json) {
    return SDUIComponent(
      id: json['id'] ?? '',
      type: ComponentType.fromString(json['type'] ?? ''),
      label: json['label'] ?? '',
      inputType: json['inputType'],
      defaultValue: json['defaultValue'],
      options: (json['options'] as List?)
          ?.map((e) => Map<String, String>.from(e))
          .toList(),
      action: json['action'],
      validations: (json['validations'] as List?)
          ?.map((e) => Map<String, dynamic>.from(e))
          .toList(),
      style: json['style'],
      imageUrl: json['imageUrl'],
      iconName: json['iconName'],
      children: (json['children'] as List?)
          ?.map((e) => SDUIComponent.fromJson(Map<String, dynamic>.from(e)))
          .toList(),
    );
  }
}
