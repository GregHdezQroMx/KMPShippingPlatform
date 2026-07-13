abstract class SDUIComponent {
  final String id;
  final String type;

  SDUIComponent({required this.id, required this.type});

  factory SDUIComponent.fromJson(Map<String, dynamic> json) {
    final type = json['type']?.toString() ?? 'unknown';
    switch (type) {
      case 'text':
        return SDUITextComponent.fromJson(json);
      case 'text_input':
        return SDUITextInputComponent.fromJson(json);
      case 'select':
        return SDUISelectComponent.fromJson(json);
      case 'button':
        return SDUIButtonComponent.fromJson(json);
      case 'image':
        return SDUIImageComponent.fromJson(json);
      case 'card':
        return SDUICardComponent.fromJson(json);
      case 'icon':
        return SDUIIconComponent.fromJson(json);
      default:
        return SDUIDummyComponent(id: json['id'] ?? '', type: type);
    }
  }
}

class SDUIDummyComponent extends SDUIComponent {
  SDUIDummyComponent({required super.id, required super.type});
}

class SDUITextComponent extends SDUIComponent {
  final String label;
  final String? style;

  SDUITextComponent({
    required super.id,
    required super.type,
    required this.label,
    this.style,
  });

  factory SDUITextComponent.fromJson(Map<String, dynamic> json) {
    return SDUITextComponent(
      id: json['id'] ?? '',
      type: 'text',
      label: json['label'] ?? '',
      style: json['style'],
    );
  }
}

class SDUITextInputComponent extends SDUIComponent {
  final String label;
  final String inputType;
  final String? defaultValue;
  final List<Map<String, dynamic>>? validations;

  SDUITextInputComponent({
    required super.id,
    required super.type,
    required this.label,
    this.inputType = 'text',
    this.defaultValue,
    this.validations,
  });

  factory SDUITextInputComponent.fromJson(Map<String, dynamic> json) {
    return SDUITextInputComponent(
      id: json['id'] ?? '',
      type: 'text_input',
      label: json['label'] ?? '',
      inputType: json['inputType'] ?? 'text',
      defaultValue: json['defaultValue'],
      validations: (json['validations'] as List?)?.map((e) => Map<String, dynamic>.from(e)).toList(),
    );
  }
}

class SDUISelectComponent extends SDUIComponent {
  final String label;
  final String? defaultValue;
  final List<Map<String, String>> options;

  SDUISelectComponent({
    required super.id,
    required super.type,
    required this.label,
    this.defaultValue,
    required this.options,
  });

  factory SDUISelectComponent.fromJson(Map<String, dynamic> json) {
    return SDUISelectComponent(
      id: json['id'] ?? '',
      type: 'select',
      label: json['label'] ?? '',
      defaultValue: json['defaultValue'],
      options: (json['options'] as List?)
              ?.map((e) => Map<String, String>.from(e))
              .toList() ??
          [],
    );
  }
}

class SDUIButtonComponent extends SDUIComponent {
  final String label;
  final String? style;
  final Map<String, dynamic> action;

  SDUIButtonComponent({
    required super.id,
    required super.type,
    required this.label,
    this.style,
    required this.action,
  });

  factory SDUIButtonComponent.fromJson(Map<String, dynamic> json) {
    return SDUIButtonComponent(
      id: json['id'] ?? '',
      type: 'button',
      label: json['label'] ?? '',
      style: json['style'],
      action: Map<String, dynamic>.from(json['action'] ?? {}),
    );
  }
}

class SDUIImageComponent extends SDUIComponent {
  final String imageUrl;
  final String? label;

  SDUIImageComponent({
    required super.id,
    required super.type,
    required this.imageUrl,
    this.label,
  });

  factory SDUIImageComponent.fromJson(Map<String, dynamic> json) {
    return SDUIImageComponent(
      id: json['id'] ?? '',
      type: 'image',
      imageUrl: json['imageUrl'] ?? '',
      label: json['label'],
    );
  }
}

class SDUICardComponent extends SDUIComponent {
  final List<SDUIComponent> children;
  final String? label;

  SDUICardComponent({
    required super.id,
    required super.type,
    required this.children,
    this.label,
  });

  factory SDUICardComponent.fromJson(Map<String, dynamic> json) {
    return SDUICardComponent(
      id: json['id'] ?? '',
      type: 'card',
      label: json['label'],
      children: (json['children'] as List?)
              ?.map((e) => SDUIComponent.fromJson(Map<String, dynamic>.from(e)))
              .toList() ??
          [],
    );
  }
}

class SDUIIconComponent extends SDUIComponent {
  final String iconName;
  final String? style;
  final String? label;

  SDUIIconComponent({
    required super.id,
    required super.type,
    required this.iconName,
    this.style,
    this.label,
  });

  factory SDUIIconComponent.fromJson(Map<String, dynamic> json) {
    return SDUIIconComponent(
      id: json['id'] ?? '',
      type: 'icon',
      iconName: json['iconName'] ?? 'help',
      style: json['style'],
      label: json['label'],
    );
  }
}
